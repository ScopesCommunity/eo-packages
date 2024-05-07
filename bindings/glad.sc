switch operating-system
case 'linux
    shared-library "libglad.so"
case 'windows
    shared-library "libglad.dll"
default
    error "Unsupported OS"

using import include

header := include "glad/glad.h"

do
    using header.typedef filter "^GL(.+)$"
    using header.define  filter "^(GL_.+)$"
    using header.define  filter "^gl([A-Z].+)$"
    gladLoadGL := header.extern.gladLoadGL

    local-scope;
