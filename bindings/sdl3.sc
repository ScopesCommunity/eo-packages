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

inline augment-enum (T prefix)
    local old-symbols : (Array Symbol)
    for k v in ('symbols T)
        field-name := k as Symbol as string
        match? start end := 'match? (.. str"^" prefix) field-name
        if match?
            new-name := rslice field-name end
            'set-symbol T (Symbol new-name) v
            'append old-symbols (k as Symbol)

    for s in old-symbols
        sc_type_del_symbol T s

augment-enum sdl.EventType "SDL_EVENT_"
augment-enum sdl.JoystickConnectionState "SDL_JOYSTICK_CONNECTION_"
augment-enum sdl.KeyCode "SDLK_"
augment-enum sdl.Keymod "SDL_KMOD_"
augment-enum sdl.MessageBoxButtonFlags "SDL_MESSAGEBOX_BUTTON_"
augment-enum sdl.MessageBoxFlags "SDL_MESSAGEBOX_"
augment-enum sdl.PowerState "SDL_POWERSTATE_"
augment-enum sdl.Scancode "SDL_SCANCODE_"
augment-enum sdl.WindowFlags "SDL_WINDOW_"

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
