switch operating-system
case 'linux
    shared-library "libstb.so"
case 'windows
    shared-library "stb.dll"
default
    error "Unsupported OS"

using import ffi-helper

# because stb_truetype depends on some types from rect_pack, the way we built it.
stbrp := ((import .rect-pack) . !header)
run-stage;

let header =
    include "stb_truetype.h"
        using stbrp

..
    filter-scope header.extern "^stbtt_"
    filter-scope header.typedef "^stbtt_"
