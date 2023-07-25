switch operating-system
case 'linux
    shared-library "libstb.so"
case 'windows
    shared-library "stb.dll"
default
    error "Unsupported OS"

using import ffi-helper

let header =
    include "stb_image_resize.h"

..
    filter-scope header.extern "^stbir_"
    filter-scope header.typedef "^stbir_"
