#include "postgres.h"
#include "c.h"
#include "executor/spi.h"
#include "pljelog.h"
#include "plantable.h"
#include "catalog/pg_type.h"


void** plantable;
int plantable_size = -1;

#define DEFAULT_PLANTABLE_SIZE			512

int
store_plantable(void* plan) {
	int i;
	void** new_plantable = NULL;
	if(plantable_size == -1)
		init_plantable();
	for(i = 0; i < plantable_size; i++) {
		if(plantable[i] == NULL) {
			plantable[i] = plan;
			return i;
		}
	}

	new_plantable = malloc(plantable_size + 16);
	for(i = 0; i <  plantable_size; i++){
		new_plantable[i] = plantable[i];
	}

	new_plantable[plantable_size] = plan;
	plantable_size += 16;
	free(plantable);
	plantable = new_plantable;

	return i+1;
}

int     remove_plantable_entry(unsigned int index){
	if(plantable == NULL)
		return -1;
	if(plantable_size <= index)
		return -1;
	SPI_freeplan(plantable[index]);
	plantable[index] = NULL;
	return index;
}

void
init_plantable(void) {
	int i;
	plantable = malloc(DEFAULT_PLANTABLE_SIZE * sizeof(Oid));
	for(i = 0; i< DEFAULT_PLANTABLE_SIZE; i++) {
		plantable[i] = NULL;
	}
	plantable_size = DEFAULT_PLANTABLE_SIZE;
}

