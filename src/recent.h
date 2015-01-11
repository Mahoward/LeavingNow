#pragma once
#ifndef RECENT_H_
#define RECENT_H_
  
#include <pebble.h>
char recent_name[4][70];

void populate_recents();
void recent_list_callback(int index, void *ctx);

#endif