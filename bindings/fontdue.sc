switch operating-system
case 'linux
    shared-library "libfontdue_native.so"
case 'windows
    shared-library "fontdue_native.dll"
default
    error "Unsupported OS"

using import include

header := include "fontdue.h"

do
    using header.extern  filter "^ftd_(.+)$"
    using header.typedef filter "^FTD_(.+)$"
    local-scope;
