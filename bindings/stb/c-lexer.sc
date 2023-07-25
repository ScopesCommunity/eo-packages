switch operating-system
case 'linux
    shared-library "libstb.so"
case 'windows
    shared-library "stb.dll"
default
    error "Unsupported OS"

using import ffi-helper

let header =
    include "stb_c_lexer.h"

..
    filter-scope header.extern "^stb_c_lexer_"
    filter-scope header.typedef "^stb_"
