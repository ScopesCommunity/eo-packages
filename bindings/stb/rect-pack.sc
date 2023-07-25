switch operating-system
case 'linux
    shared-library "libstb.so"
case 'windows
    shared-library "stb.dll"
default
    error "Unsupported OS"

using import ffi-helper

let header =
    include "stb_rect_pack.h"

'bind
    ..
        filter-scope header.typedef "^stbrp_"
        filter-scope header.extern "^stbrp_"
    '!header
    header
