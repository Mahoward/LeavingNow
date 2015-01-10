#include <pebble.h>

#include "mainmenu.h"

#define NUM_MM_SECTIONS 1
#define NUM_MM_ITEMS 3

static Window *main_window;
static Window *recent_window;
static Window *fav_window;
static Window *contacts_window;

static SimpleMenuLayer *main_menu_layer;
static SimpleMenuLayer *recent_menu_layer;
static SimpleMenuLayer *fav_menu_layer;
static SimpleMenuLayer *contacts_menu_layer;

static SimpleMenuSection main_menu_sections[NUM_MM_SECTIONS];

static SimpleMenuItem main_menu[NUM_MM_ITEMS];

static GBitmap *menu_icon_image;

static void recent_list_callback(int index, void *ctx) {
  main_menu[index].subtitle = "Selected";
  layer_mark_dirty(simple_menu_layer_get_layer(main_menu_layer));
}

static void fav_list_callback(int index, void *ctx) {
  main_menu[index].subtitle = "Selected";
  layer_mark_dirty(simple_menu_layer_get_layer(main_menu_layer));
}

static void contact_list_callback(int index, void *ctx) {
  main_menu[index].subtitle = "Selected";
  layer_mark_dirty(simple_menu_layer_get_layer(main_menu_layer));
}

static void main_window_load(Window *window) {
  int curr_item = 0;

  main_menu[curr_item++] = (SimpleMenuItem){
    .title = "Recent",
    .callback = recent_list_callback,
  };

  main_menu[curr_item++] = (SimpleMenuItem){
    .title = "Favorites",
    .callback = fav_list_callback,
  };

  main_menu[curr_item++] = (SimpleMenuItem){
    .title = "Contacts",
    .callback = contact_list_callback,
    .icon = menu_icon_image,
  };

  main_menu_sections[0] = (SimpleMenuSection){
    .num_items = NUM_MM_ITEMS,
    .items = main_menu,
  };
  
  Layer *window_layer = window_get_root_layer(window);
  GRect bounds = layer_get_frame(window_layer);

  main_menu_layer = simple_menu_layer_create(bounds, window, main_menu_sections, NUM_MM_SECTIONS, NULL);

  layer_add_child(window_layer, simple_menu_layer_get_layer(main_menu_layer));
}

void main_window_unload(Window *window) {
  simple_menu_layer_destroy(main_menu_layer);

  gbitmap_destroy(menu_icon_image);
}

static void init() {
  main_window = window_create();

  window_set_window_handlers(main_window, (WindowHandlers) {
    .load = main_window_load,
    .unload = main_window_unload,
  });

  window_stack_push(main_window, true);

}

static void deinit(){
  window_destroy(main_window);
}

int main(void){
  init();
  app_event_loop();
  deinit();
}