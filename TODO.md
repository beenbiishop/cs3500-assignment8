# Assignment 6 To-Do List

## Model

1. Create image utility class
    * Static method to get histogram data from an image object
    * Static method to convert an image object to a BufferedImage object

## Controller

1. [x] Add renderWelcome as a private method to the controller
2. [x] Update renderMenu command to use renderMessage instead

## View

1. [x] Remove renderMenu and renderWelcome from interface
2. [x] Remove renderMenu and renderWelcome from implementation
3. [ ] Add new interface that extends ImageProcessorView
4. [ ] Add new implementation that implements the new interface

### Constructor

1. Takes in a list/map of supported transformations, tooltips, etc
2. Takes in the command used for loading
3. Takes in the command used for saving

### New Methods

1. Add Image
    * Adds an image tab with the image file name as the key and the BufferedImage object as the
      value
    * If a tab with the same name already exists, the image object will be replaced
2. Remove Image
    * Removes the image tab from the view with the given file name
    * Checks to ensure the image file name is already a tab
3. Render Dialog
    * Renders a popup with the type (success, warning, danger, note {bigger}) as an enum and the
      string to display
    * All popups just have an "ok" button to be closed by the user
4. Render Input Dialog
    * Renders a popup with the list of strings to display as labels and a string as an optional
      error message (for if it is the second attempt)
    * Renders a text field for each label
    * Returns a list of strings with the entries or throws error if cancelled
5. Render Image Pick Dialog
    * Takes in type as an enum (load/save)
    * Returns string of file path or throws error if cancelled

### Existing Methods

1. Render Message
    * Replaces the JLabel at the bottom of the screen with the given string (for messages a user
      doesn't need to acknowledge)

## Testing

1. [ ] xxx

## Other

1. [ ] xxx