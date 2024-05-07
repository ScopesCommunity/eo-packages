switch operating-system
case 'linux
    shared-library "libstb.so"
case 'windows
    shared-library "stb.dll"
default
    error "Unsupported OS"

using import include

let header =
    include "stb_vorbis.c"
        options "-DSTB_VORBIS_HEADER_ONLY"

do
    using header.extern  filter "^stb_vorbis_(.+)$"
    using header.typedef filter "^stb_vorbis_(.+)$"
    local-scope;
