switch operating-system
case 'linux
    shared-library "libstb.so"
case 'windows
    shared-library "stb.dll"
default
    error "Unsupported OS"

using import include

let header =
    include "stb_image.h"

do
    using header.extern filter "^stbi_(.+)$"
    local-scope;
