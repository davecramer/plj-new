
/**
 * file name:			plpgj_hook.c
 * description:			PL/pgJ call hook.
 * author:				Laszlo Hornyak
 */

#include "postgres.h"
#include "fmgr.h"
#include "plpgj_messages.h"
#include "plpgj_channel.h"
#include "plpgj_message_fns.h"
#include "plpgj_core.h"

#include "utils/portal.h"
#include "lib/stringinfo.h"
#include "nodes/makefuncs.h"
#include "parser/parse_type.h"
#include "module_config.h"
#include "commands/trigger.h"

#include "utils/palloc.h"
#include "utils/memutils.h"
#include "access/xact.h"
#include "pljelog.h"

/*	*/

/* proto */

/*	*/

/**
 * Called for handling an exception from the java backend.
 */
void		plpgj_exception_do(error_message);

/**
 * Called for handling sql calls from the java backend.
 */
void		plpgj_sql_do(sql_msg msg);

/**
 * Called for handling results from the java backend.
 */
Datum		plpgj_result_do(plpgj_result);

void		plpgj_log_do(log_message);

void		plpgj_EOXactCallBack(bool isCommit, void *arg);

void		plpgj_ErrorContextCallback(void *arg);

int			callbacks_init = 0;

/*	*/

/* impl */

/*	*/

Datum
plpgj_call_hook(PG_FUNCTION_ARGS)
{

	message		req;
	int			message_type;
	ErrorContextCallback *mycallback;

	if (!plpgj_channel_initialized())
	{
		pljelog(DEBUG1, "initing channel");
		plpgj_channel_initialize();
	}

	/*
	 * register event handlers
	 */

	if (!callbacks_init)
	{
		mycallback = SPI_palloc(sizeof(ErrorContextCallback));
		mycallback->previous = error_context_stack;
		pljelog(DEBUG1, "1");
		mycallback->callback = plpgj_ErrorContextCallback;
		pljelog(DEBUG1, "2");
		mycallback->arg = "hello world!";
		pljelog(DEBUG1, "3");

		error_context_stack = mycallback;
		pljelog(DEBUG1, "4");

		RegisterEOXactCallback(plpgj_EOXactCallBack, NULL);
		pljelog(DEBUG1, "5");
		callbacks_init = 1;
	}
	/*
	 * now do the real job
	 */

	pljelog(DEBUG1, "7");

	if (CALLED_AS_TRIGGER(fcinfo)
		&& plj_get_configvalue_boolean("plpgj.core.usetriggers"))
	{
		req = (message) plpgj_create_trigger_call(fcinfo);
		pljelog(DEBUG1, "8");
	}
	else
	{
		pljelog(DEBUG1, "9");
		req = (message) plpgj_create_call(fcinfo);
		pljelog(DEBUG1, "10");
	}

	plpgj_channel_send((message) req);
	free_message(req);

	do
	{
		void	   *ansver = NULL;

		pljelog(DEBUG1, "send message");
		ansver = plpgj_channel_receive();
		message_type = plpgj_message_type(ansver);
		switch (message_type)
		{
			case MT_RESULT:
				pljelog(DEBUG1, "received: result");

				break;
			case MT_EXCEPTION:
				pljelog(DEBUG1, "received: exception");
				plpgj_exception_do((error_message) ansver);
				PG_RETURN_NULL();
				break;
			case MT_SQL:
				{
					pljelog(DEBUG1, "received: sql");
					plpgj_sql_do(ansver);
				}
				break;
			case MT_LOG:
				plpgj_log_do((log_message) ansver);
				break;
			case MT_TUPLRES:
				pljelog(DEBUG1, "received: tupleresult");

				break;
			default:
				pljelog(FATAL, "received: unknown message.");
				/*
				 * TODO perhaps there is a more elegant way to escape from here.
				 */
				PG_RETURN_NULL();
		}
		free_message(ansver);

		/*
		 * here is how to escape from the loop
		 */
		pljelog(DEBUG1, "begin return procedure");

		if (message_type == MT_RESULT)
		{
			/*
			 * TODO what now? we should return a value HERE.
			 */
			/*
			 * PG_RETURN...
			 */


			plpgj_result res = (plpgj_result) ansver;

			if (res->rows == 1 && res->cols == 1)
			{
				Datum		ret;
				HeapTuple	typetup;
				Form_pg_type type;
				TypeName   *typeName;
				Oid			typeOid;
				Datum		rawDatum;
				StringInfo	rawString;
				MemoryContext oldctx;

				if (res->data[0][0].isnull == 1)
					error_context_stack = error_context_stack->previous;
				PG_RETURN_NULL();

				typeName = makeTypeName(res->types[0]);
				typeOid = typenameTypeId(typeName);
				typetup = SearchSysCache(TYPEOID, typeOid, 0, 0, 0);
				if (!HeapTupleIsValid(typetup))

					type = (Form_pg_type) GETSTRUCT(typetup);

				/*
				 */
				/*
				 * (see http://jira.codehaus.org/secure/ViewIssue.jspa?key=PLJ-1)
				 */
				/*
				 */

				oldctx = CurrentMemoryContext;
				MemoryContextSwitchTo(QueryContext);

				rawString = SPI_palloc(sizeof(StringInfoData));
				initStringInfo(rawString);
				/*
				 * {
				 * int i = 0;
				 * for(i = 0; i<res->data[0][0].length; i++){
				 * pljelog(DEBUG1,">%d",(char)((char*)res->data[0][0].data)[i]);
				 * }
				 *
				 * }
				 */
				appendBinaryStringInfo(rawString, res->data[0][0].data,
									   res->data[0][0].length);
				rawDatum = PointerGetDatum(rawString);

				ret = OidFunctionCall1(type->typreceive, rawDatum);

				/*
				 * SPI_pfree(rawString);
				 */
				ReleaseSysCache(typetup);

				MemoryContextSwitchTo(oldctx);

				return ret;
			}
			else if (res->rows == 0)
			{
				error_context_stack = error_context_stack->previous;
				PG_RETURN_VOID();
			}

			/*
			 * continue here!!
			 */


/*			PG_RETURN_NULL(); */
			/*
			 * break;
			 */
		}
		if (message_type == MT_EXCEPTION)
			PG_RETURN_NULL();
		if (message_type == MT_TUPLRES)
		{
			HeapTuple	rettup;
			trigger_tupleres res = (trigger_tupleres) ansver;

			/*
			 * if(!CALLED_AS_TRIGGER(fcinfo))
			 */
			TriggerData *tdata = (TriggerData *) fcinfo->context;

			if (TRIGGER_FIRED_FOR_STATEMENT(tdata->tg_event))
				rettup = NULL;
			else
			{
				Datum	   *datums;
				int		   *columns;
				char	   *nulls;
				int			i;

				/*
				 * copy tuple until i find out how to alloc one...
				 */
				rettup = SPI_copytuple(tdata->tg_trigtuple);
				/*
				 * rettup = SPI_palloc(HEAPTUPLESIZE);
				 */

				if (res->colcount > 0)
				{
					datums = SPI_palloc(res->colcount * sizeof(Datum));
					columns = SPI_palloc(res->colcount * sizeof(int));
					nulls = SPI_palloc(res->colcount * sizeof(char));
				}
				else
				{
					datums = NULL;
					columns = NULL;
					nulls = NULL;
				}
				for (i = 0; i < res->colcount; i++)
				{
					HeapTuple	typtup;
					Oid			typoid;
					Form_pg_type typ;
					TypeName   *typnam;
					StringInfoData *rawString;

					rawString = SPI_palloc(sizeof(StringInfoData));
					initStringInfo(rawString);
					appendBinaryStringInfo(rawString,
										   res->_tuple[i]->data.data,
										   res->_tuple[i]->data.length);

					typnam = makeTypeName(res->_tuple[i]->type);
					typoid = LookupTypeName(typnam);
					typtup = SearchSysCache(TYPEOID, typoid, 0, 0, 0);
					typ = (Form_pg_type) GETSTRUCT(typtup);


					datums[i] = OidFunctionCall1(typ->typreceive,
												 PointerGetDatum
												 (rawString));
					/*
					 * wrong
					 */
					columns[i] = SPI_fnumber
						(tdata->tg_relation->rd_att, res->colnames[i]);
					nulls[i] = ' ';
					ReleaseSysCache(typtup);
				}
				rettup = SPI_modifytuple(tdata->tg_relation,
										 rettup,
										 res->colcount,
										 columns, datums, nulls);
			}
			return PointerGetDatum(rettup);
		}
		pljelog(ERROR, "no handler");

	}
	while (1);

	PG_RETURN_NULL();

}

Datum
plpgj_result_do(plpgj_result res)
{
	return 0;
}

void
plpgj_sql_do(sql_msg msg)
{
	switch (msg->sqltype)
	{
		case SQL_TYPE_STATEMENT:
			SPI_exec(((sql_msg_statement) msg)->statement, 0);
			break;
		case SQL_TYPE_CURSOR_CLOSE:
			{
				Portal		portal;
				sql_msg_cursor_close sql_c_c = (sql_msg_cursor_close) msg;

				portal = GetPortalByName(sql_c_c->cursorname);
				if (!PortalIsValid(portal))
				{
					pljelog(ERROR, "the portal %s does not exist!",
							sql_c_c->cursorname);

					/*
					 * TODO throw back an exception!
					 */
				}

				PortalDrop(portal, 0);

			}
			break;
		case SQL_TYPE_CURSOR_OPEN:
			{
				Portal		portal;
				sql_msg_cursor_open sql_c_o = (sql_msg_cursor_open) msg;

				/*
				 * TODO: creates constantly bidirectional cursors :(
				 */
				portal = CreatePortal(sql_c_o->cursorname, 1, 1);


			}
			break;
		case SQL_TYPE_FETCH:
			{
				/*
				 * Portal portal;
				 *
				 * sql_msg_cursor_fetch sql_c_f = (sql_msg_cursor_fetch)msg;
				 *
				 * portal = GetPortalByName(sql_c_f->cursorname);
				 * if(!PortalIsValid(portal))
				 * TODO send back error.
				 */
			}
			break;
		default:
			pljelog(FATAL, "Unhandled SQL message.");
	}
}

void
plpgj_exception_do(error_message msg)
{

/*	pljelog(ERROR,"Java side exception occured: \n %s : %s \n ", msg->classname, msg->message ); */
}

void
plpgj_log_do(log_message log)
{
	int			level = DEBUG1;

	if (log == NULL)
		return;
	switch (log->level)
	{
		case 1:
			level = DEBUG5;
			break;
		case 2:
			level = INFO;
			break;
		default:
			level = INFO;
	}

	pljelog(level, "[%s] -  %s ", log->category, log->message);

}

/**
 * This may be the starting point of transaction externalisation.
 */
void
plpgj_EOXactCallBack(bool isCommit, void *arg)
{
	if (isCommit)
		pljelog(DEBUG1, "Transaction commit - plpgj");
	else
	{
		pljelog(DEBUG1, "Transaction rollback - plpgj");
	};
}

/**
 * ErrorContextCallback function
 */
void
plpgj_ErrorContextCallback(void *arg)
{


	printf("aaa\n");
	if (!pljlogging_error)
		return;
	printf("bbb\n");

	/*
	 * disable loging
	 */
	pljloging = 0;

	/*
	 *send the error to the java process.
	 */
	error_message msg = SPI_palloc(sizeof(str_error_message));

	msg->msgtype = MT_EXCEPTION;
	msg->length = sizeof(str_error_message);
	msg->classname = "PostgreSQL statement";
	msg->message = "No information (see your statement)";
	msg->stacktrace = "holla aimgos!";
	pljelog(DEBUG1, "sending exception");
	plpgj_channel_send((message) msg);

	/*
	 * pljelog(DEBUG1, "exception message sent");
	 */
	/*
	 * Unregister ErrorContextCallback
	 */
	/*
	 * error_context_stack = error_context_stack -> previous;
	 */
	pljelog(DEBUG1, "callback unregistered.");

	/*
	 * re-enable loging
	 */
	pljloging = 1;
}
