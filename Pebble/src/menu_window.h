#pragma once
#ifndef MAIN_MENU_H_
#define MAIN_MENU_H_

#include <pebble.h>  

#define NUM_MM_SECTIONS 1
#define NUM_MM_ITEMS 3

static Window *main_window;
SimpleMenuItem main_menu[NUM_MM_ITEMS];

void menu_window_load(Window *window);
void menu_window_unload(Window *window);

#endif
