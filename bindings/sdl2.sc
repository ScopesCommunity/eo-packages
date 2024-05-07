switch operating-system
case 'linux
    shared-library "libSDL2.so"
case 'windows
    shared-library "SDL2.dll"
default
    error "Unsupported OS"

using import include

header :=
    include
        """"#include <SDL2/SDL.h>
            #include <SDL2/SDL_syswm.h>
            #include <SDL2/SDL_vulkan.h>

inline enum-constructor (T)
    bitcast 0 T

for k v in header.typedef
    if (('typeof v) == type)
        v as:= type
        if (v < CEnum)
            'set-symbol v '__typecall enum-constructor

do
    using header.extern  filter "^SDL_(.+)$"
    using header.typedef filter "^SDL_(.+)$"
    using header.define  filter "^(SDL_.+)$"
    using header.const   filter "^(SDLK?_.+)$"

    inline SDL_WINDOWPOS_UNDEFINED_DISPLAY (X)
        SDL_WINDOWPOS_UNDEFINED_MASK | X

    inline SDL_WINDOWPOS_ISUNDEFINED (X)
        (X & 0xFFFF0000) == SDL_WINDOWPOS_UNDEFINED_MASK

    inline SDL_WINDOWPOS_CENTERED_DISPLAY (X)
        SDL_WINDOWPOS_CENTERED_MASK | X

    inline SDL_WINDOWPOS_ISCENTERED (X)
        (X & 0xFFFF0000) == SDL_WINDOWPOS_CENTERED_MASK

    let SDL_WINDOWPOS_CENTERED = (SDL_WINDOWPOS_CENTERED_DISPLAY 0)
    let SDL_WINDOWPOS_UNDEFINED = (SDL_WINDOWPOS_UNDEFINED_DISPLAY 0)

    inline SDL_VERSION (version-struct)
        version-struct.major = SDL_MAJOR_VERSION
        version-struct.minor = SDL_MINOR_VERSION
        version-struct.patch = SDL_PATCHLEVEL
        ;

    inline SDL_VERSIONNUM (major minor patch)
        (major * 1000) + (minor * 100) + patch

    inline SDL_COMPILEDVERSION ()
        SDL_VERSIONNUM SDL_MAJOR_VERSION SDL_MINOR_VERSION SDL_PATCHLEVEL

    inline SDL_VERSION_ATLEAST (major minor patch)
        (SDL_COMPILEDVERSION) >= (SDL_VERSIONNUM major minor patch)

    let SDL_INIT_EVERYTHING =
        SDL_INIT_TIMER | SDL_INIT_AUDIO | SDL_INIT_VIDEO | SDL_INIT_EVENTS | \
        SDL_INIT_JOYSTICK | SDL_INIT_HAPTIC | SDL_INIT_GAMECONTROLLER | SDL_INIT_SENSOR

    local-scope;
