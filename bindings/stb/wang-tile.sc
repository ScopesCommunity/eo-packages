switch operating-system
case 'linux
    shared-library "libstb.so"
case 'windows
    shared-library "stb.dll"
default
    error "Unsupported OS"

using import ffi-helper

let header =
    include "stb_herringbone_wang_tile.h"

..
    filter-scope header.extern "^stbhw_"
    filter-scope header.typedef "^stbhw_"
