switch operating-system
case 'linux
    shared-library "libminiaudio.so"
case 'windows
    shared-library "miniaudio.dll"
default
    error "Unsupported OS"

#FIXME: change to include module once it's fixed
header := deprecated-include "miniaudio.h"

do
    using header.extern  filter "^ma_(.+)$"
    using header.typedef filter "^ma_(.+)$"
    using header.define  filter "^(ma_.+)$"
    using header.const   filter "^(MA_.+)$"
    local-scope;
