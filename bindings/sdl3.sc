switch operating-system
case 'linux
    shared-library "libSDL3.so"
case 'windows
    shared-library "SDL3.dll"
default
    error "Unsupported OS"

using import Array slice

header := include "SDL3/SDL.h"

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
        inline SDL_WINDOWPOS_UNDEFINED_DISPLAY (X)
            sdl.SDL_WINDOWPOS_UNDEFINED_MASK | X

        inline SDL_WINDOWPOS_ISUNDEFINED (X)
            (X & 0xFFFF0000) == sdl.SDL_WINDOWPOS_UNDEFINED_MASK

        inline SDL_WINDOWPOS_CENTERED_DISPLAY (X)
            sdl.SDL_WINDOWPOS_CENTERED_MASK | X

        inline SDL_WINDOWPOS_ISCENTERED (X)
            (X & 0xFFFF0000) == sdl.SDL_WINDOWPOS_CENTERED_MASK

        SDL_WINDOWPOS_CENTERED  := SDL_WINDOWPOS_CENTERED_DISPLAY 0
        SDL_WINDOWPOS_UNDEFINED := SDL_WINDOWPOS_UNDEFINED_DISPLAY 0

        inline SDL_VERSION (version-struct)
            version-struct.major = sdl.SDL_MAJOR_VERSION
            version-struct.minor = sdl.SDL_MINOR_VERSION
            version-struct.patch = sdl.SDL_PATCHLEVEL
            ;

        inline SDL_VERSIONNUM (major minor patch)
            (major << 24) | (minor << 8) | patch

        inline SDL_COMPILEDVERSION ()
            SDL_VERSIONNUM sdl.SDL_MAJOR_VERSION sdl.SDL_MINOR_VERSION sdl.SDL_PATCHLEVEL

        inline SDL_VERSION_ATLEAST (major minor patch)
            (SDL_COMPILEDVERSION) >= (SDL_VERSIONNUM major minor patch)

        local-scope;

sdl .. sdl-macros
