switch operating-system
case 'linux
    shared-library "libSDL2.so"
case 'windows
    shared-library "SDL2.dll"
default
    error "Unsupported OS"

using import ffi-helper

let header =
    include
        """"#include <SDL2/SDL.h>
            #include <SDL2/SDL_syswm.h>
            #include <SDL2/SDL_vulkan.h>

let sdl-extern = (filter-scope header.extern "^SDL_")
let sdl-typedef = (filter-scope header.typedef "^SDL_")
let sdl-define = (filter-scope header.define "^(?=SDL_)")
let sdl-const = (filter-scope header.const "^(?=SDLK?_)")

let sdl = (.. sdl-extern sdl-typedef sdl-define sdl-const)
run-stage;

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

        let SDL_WINDOWPOS_CENTERED = (SDL_WINDOWPOS_CENTERED_DISPLAY 0)
        let SDL_WINDOWPOS_UNDEFINED = (SDL_WINDOWPOS_UNDEFINED_DISPLAY 0)

        inline SDL_VERSION (version-struct)
            version-struct.major = sdl.SDL_MAJOR_VERSION
            version-struct.minor = sdl.SDL_MINOR_VERSION
            version-struct.patch = sdl.SDL_PATCHLEVEL
            ;

        inline SDL_VERSIONNUM (major minor patch)
            (major * 1000) + (minor * 100) + patch

        inline SDL_COMPILEDVERSION ()
            SDL_VERSIONNUM sdl.SDL_MAJOR_VERSION sdl.SDL_MINOR_VERSION sdl.SDL_PATCHLEVEL

        inline SDL_VERSION_ATLEAST (major minor patch)
            (SDL_COMPILEDVERSION) >= (SDL_VERSIONNUM major minor patch)

        let SDL_INIT_EVERYTHING =
            sdl.SDL_INIT_TIMER | sdl.SDL_INIT_AUDIO | sdl.SDL_INIT_VIDEO | sdl.SDL_INIT_EVENTS | \
            sdl.SDL_INIT_JOYSTICK | sdl.SDL_INIT_HAPTIC | sdl.SDL_INIT_GAMECONTROLLER | sdl.SDL_INIT_SENSOR
        locals;

inline enum-constructor (T)
    bitcast 0 T

for scope in ('lineage sdl)
    for k v in scope
        if (('typeof v) == type)
            v as:= type
            if (v < CEnum)
                'set-symbol v '__typecall enum-constructor

sdl .. sdl-macros
