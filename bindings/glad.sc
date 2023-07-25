switch operating-system
case 'linux
    shared-library "libglad.so"
case 'windows
    shared-library "libglad.dll"
default
    error "Unsupported OS"

using import ffi-helper

let header =
    include
        """"#include <glad/glad.h>

let glad-typedef = (filter-scope header.typedef "^GL")
let glad-define = (filter-scope header.define "^((?=GL_)|gl(?=[A-Z]))")

'bind
    .. glad-typedef glad-define
    'gladLoadGL
    header.extern.gladLoadGL
