#include "pebble.h"

#define NUM_MM_SECTIONS 1
#define NUM_ITEMS 3

static Window *window;

// This is a simple menu layer
static SimpleMenuLayer *simple_menu_layer;

// A simple menu layer can have multiple sections
static SimpleMenuSection menu_sections[NUM_MM_SECTIONS];

// Each section is composed of a number of menu items
static SimpleMenuItem main_menu[NUM_ITEMS];

// Menu items can optionally have icons
static GBitmap *menu_icon_image;

// You can capture when the user selects a menu icon with a menu item select callback
static void menu_select_callback(int index, void *ctx) {
  // Here we just change the subtitle to a literal string
  main_menu[index].subtitle = "You've hit select here!";
  // Mark the layer to be updated
  layer_mark_dirty(simple_menu_layer_get_layer(simple_menu_layer));
}

// This initializes the menu upon window load
static void window_load(Window *window) {
  // We'll have to load the icon before we can use it
  // Although we already defined NUM_RECENTS, you can define
  // an int as such to easily change the order of menu items later
  int curr_item = 0;

  // This is an example of how you'd set a simple menu item
  main_menu[curr_item++] = (SimpleMenuItem){
    // You should give each menu item a title and callback
    .title = "Recent",
    .callback = menu_select_callback,
  };
  // The menu items appear in the order saved in the menu items array
  main_menu[curr_item++] = (SimpleMenuItem){
    .title = "Favorites",
    // You can also give menu items a subtitle
    .subtitle = "Here's a subtitle",
    .callback = menu_select_callback,
  };
  main_menu[curr_item++] = (SimpleMenuItem){
    .title = "Contacts",
    .subtitle = "This has an icon",
    .callback = menu_select_callback,
    // This is how you would give a menu item an icon
    .icon = menu_icon_image,
  };

  // Bind the menu items to the corresponding menu sections
  menu_sections[0] = (SimpleMenuSection){
    .num_items = NUM_ITEMS,
    .items = main_menu,
  };
  
  // Now we prepare to initialize the simple menu layer
  // We need the bounds to specify the simple menu layer's viewport size
  // In this case, it'll be the same as the window's
  Layer *window_layer = window_get_root_layer(window);
  GRect bounds = layer_get_frame(window_layer);

  // Initialize the simple menu layer
  simple_menu_layer = simple_menu_layer_create(bounds, window, menu_sections, NUM_MM_SECTIONS, NULL);

  // Add it to the window for display
  layer_add_child(window_layer, simple_menu_layer_get_layer(simple_menu_layer));
}

// Deinitialize resources on window unload that were initialized on window load
void window_unload(Window *window) {
  simple_menu_layer_destroy(simple_menu_layer);

  // Cleanup the menu icon
  gbitmap_destroy(menu_icon_image);
}

int main(void) {
  window = window_create();

  // Setup the window handlers
  window_set_window_handlers(window, (WindowHandlers) {
    .load = window_load,
    .unload = window_unload,
  });

  window_stack_push(window, true /* Animated */);

  app_event_loop();

  window_destroy(window);
}