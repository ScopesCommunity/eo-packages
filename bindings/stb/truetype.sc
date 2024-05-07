switch operating-system
case 'linux
    shared-library "libstb.so"
case 'windows
    shared-library "stb.dll"
default
    error "Unsupported OS"

# because stb_truetype depends on some types from rect_pack, the way we built it.
stbrp := ((import .rect-pack) . !header)
run-stage;

using import include

let header =
    include "stb_truetype.h"
        using stbrp

do
    using header.extern  filter "^stbtt_(.+)$"
    using header.typedef filter "^stbtt_(.+)$"
    local-scope;
