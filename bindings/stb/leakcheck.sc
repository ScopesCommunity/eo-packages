switch operating-system
case 'linux
    shared-library "libstb.so"
case 'windows
    shared-library "stb.dll"
default
    error "Unsupported OS"

using import ffi-helper

let header =
    include "stb_leakcheck.h"

filter-scope header.extern "^stb_leakcheck_"
