switch operating-system
case 'linux
    shared-library "libstb.so"
case 'windows
    shared-library "stb.dll"
default
    error "Unsupported OS"

using import include

let header =
    include "stb_herringbone_wang_tile.h"

do
    using header.extern  filter "^stbhw_(.+)$"
    using header.typedef filter "^stbhw_(.+)$"
    local-scope;
