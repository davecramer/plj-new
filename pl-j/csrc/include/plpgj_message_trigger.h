#ifndef PLPGJ_MESSAGE_TRIGREQ_H
#define PLPGJ_MESSAGE_TRIGREQ_H

#include "plpgj_message_base.h"

#define PLPGJ_TRIGGER_REASON_INSERT 1
#define PLPGJ_TRIGGER_REASON_UPDATE 2
#define PLPGJ_TRIGGER_REASON_DELETE 3

#define PLPGJ_TRIGGER_ACTIONORDER_BEFORE 1
#define PLPGJ_TRIGGER_ACTIONORDER_AFTER 2

#define PLPGJ_TRIGGER_STARTED_FOR_ROW 0
#define PLPGJ_TRIGGER_STARTED_FOR_STATEMENT 1

typedef struct
{
	base_message_content;
	/*
	 * class name
	 */
	char		classname[50];
	/*
	 * method name
	 */
	char		methodname[50];
	char	   *tablename;
	short		reason;
	short		actionorder;
	short		row;
	int			colcount;
	char	  **colnames;
	char	  **coltypes;
	pparam	   *_new;
	pparam	   *_old;
} str_msg_trigger_callreq;

typedef str_msg_trigger_callreq *trigger_callreq;

typedef struct
{
	base_message_content;
	char	   *tablename;
	char	  **colnames;
	int			colcount;
	pparam	   *_tuple;
} str_msg_trigger_tupleresult;

typedef str_msg_trigger_tupleresult *trigger_tupleres;

#endif
