#pragma once
#ifndef CONTACTS_H_
#define CONTACTS_H_
  
#include <pebble.h>

char contacts_name[4][70];

void populate_contacts();
void contact_list_callback(int index, void *ctx);

#endif