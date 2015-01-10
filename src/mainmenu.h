#ifndef mainmenu_H_
#define mainmenu_H_
  
#include <pebble.h>

#define NUM_MM_SECTIONS 1
#define NUM_MM_ITEMS 3
  
static SimpleMenuLayer *main_menu_layer;
static Window *main_window;
static SimpleMenuSection main_menu_sections[NUM_MM_SECTIONS];
static SimpleMenuItem main_menu[NUM_MM_ITEMS];
static GBitmap *menu_icon_image;


static void main_window_load(Window *window);
void main_window_unload(Window *window);

#endif