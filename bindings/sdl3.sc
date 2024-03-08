switch operating-system
case 'linux
    shared-library "libSDL2.so"
case 'windows
    shared-library "SDL2.dll"
default
    error "Unsupported OS"

using import Array slice

header := include "SDL2/SDL.h"

vvv bind sdl
do
    using header.extern filter "^SDL_(.+)$"
    using header.typedef filter "^SDL_(.+)$"
    using header.define filter "^(SDL_.+)$"
    using header.const filter "^(SDLK?_.+)"
    local-scope;

inline enum-constructor (T)
    bitcast 0 T

for k v in header.typedef
    T := v as type
    if (T < CEnum)
        'set-symbol T '__typecall enum-constructor

        local old-symbols : (Array Symbol)
        tname := k as Symbol as string
        for k v in ('symbols T)
            field-name := k as Symbol as string
            if ('match? str"^SDLK?_" field-name)
                new-name := rslice field-name ((countof tname) + 1) # _
                'set-symbol T (Symbol new-name) v
                'append old-symbols (k as Symbol)

        for s in old-symbols
            sc_type_del_symbol T s

let sdl-macros =
    do
        local-scope;

sdl .. sdl-macros
