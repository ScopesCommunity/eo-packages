switch operating-system
case 'linux
    shared-library "libglfw.so"
case 'windows
    shared-library "glfw3.dll"
default
    error "Unsupported OS"

using import include

header := include "GLFW/glfw3.h"
                                                           
do
    using header.extern  filter "^glfw(.+)$"
    using header.typedef filter "^GLFW(.+)$"
    using header.define  filter "^(GLFW_.+)$"

    local-scope;
