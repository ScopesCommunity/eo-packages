switch operating-system
case 'linux
    shared-library "libglad.so"
case 'windows
    shared-library "libglad.dll"
default
    error "Unsupported OS"

using import slice

inline filter-scope (scope pattern)
    pattern as:= string
    fold (scope = (Scope)) for k v in scope
        let name = (k as Symbol as string)
        let match? start end = ('match? pattern name)
        if match?
            'bind scope (Symbol (rslice name end)) v
        else
            scope

let header =
    include
        """"#include <glad/glad.h>

let glad-typedef = (filter-scope header.typedef "^GL")
let glad-define = (filter-scope header.define "^((?=GL_)|gl(?=[A-Z]))")

'bind
    .. glad-typedef glad-define
    'gladLoadGL
    header.extern.gladLoadGL
