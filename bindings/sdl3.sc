switch operating-system
case 'linux
    shared-library "libSDL2.so"
case 'windows
    shared-library "SDL2.dll"
default
    error "Unsupported OS"

using import ffi-helper

header := include "SDL2/SDL.h"

vvv bind sdl
do
    using header.extern filter "^SDL_(.+)$"
    using header.typedef filter "^SDL_(.+)$"
    using header.define filter "^(SDL_.+)$"
    using header.const filter "^(SDLK?_.+)"
    local-scope;
run-stage;

let sdl-macros =
    do
        local-scope;

inline enum-constructor (T)
    bitcast 0 T

for scope in ('lineage sdl)
    for k v in scope
        if (('typeof v) == type)
            v as:= type
            if (v < CEnum)
                'set-symbol v '__typecall enum-constructor

sdl .. sdl-macros
