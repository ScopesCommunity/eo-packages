switch operating-system
case 'linux
    shared-library "libstb.so"
case 'windows
    shared-library "stb.dll"
default
    error "Unsupported OS"

using import include

let header =
    include "stb_rect_pack.h"

do
    using header.typedef filter "^stbrp_(.+)$"
    using header.extern  filter "^stbrp_(.+)$"
    let !header = header
    local-scope;
