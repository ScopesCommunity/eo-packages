switch operating-system
case 'linux
    shared-library "libvolk.so"
case 'windows
    shared-library "libvolk.dll"
default
    error "Unsupported OS"

using import include

header := include "volk.h"

do
    using header.extern  filter "^vk(.+)$"
    using header.extern  filter "^(volk.+)$"
    using header.typedef filter "^Vk(.+)$"
    using header.define  filter "^(VK_.+)$"
    local-scope;
