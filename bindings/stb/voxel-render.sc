switch operating-system
case 'linux
    shared-library "libstb.so"
case 'windows
    shared-library "stb.dll"
default
    error "Unsupported OS"

using import ffi-helper

let header =
    include "stb_voxel_render.h"

..
    filter-scope header.extern "^stbvox_"
    filter-scope header.typedef "^stbvox_"
