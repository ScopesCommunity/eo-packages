switch operating-system
case 'linux
    shared-library "libstb.so"
case 'windows
    shared-library "stb.dll"
default
    error "Unsupported OS"

using import ffi-helper

let header =
    include "stb_vorbis.c"
        options "-DSTB_VORBIS_HEADER_ONLY"

..
    filter-scope header.extern "^stb_vorbis_"
    filter-scope header.typedef "^stb_vorbis_"
