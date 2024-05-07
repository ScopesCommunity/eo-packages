switch operating-system
case 'linux
    shared-library "libstb.so"
case 'windows
    shared-library "stb.dll"
default
    error "Unsupported OS"

using import include

let header =
    include "stb_image_resize2.h"

do
    using header.extern  filter "^stbir_(.+)$"
    using header.typedef filter "^stbir_(.+)$"
    local-scope;
