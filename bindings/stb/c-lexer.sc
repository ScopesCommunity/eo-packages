switch operating-system
case 'linux
    shared-library "libstb.so"
case 'windows
    shared-library "stb.dll"
default
    error "Unsupported OS"

using import include

let header =
    include "stb_c_lexer.h"

do
    using header.extern  filter "^stb_c_lexer_(.+)$"
    using header.typedef filter "^stb_(.+)$"
    local-scope;
