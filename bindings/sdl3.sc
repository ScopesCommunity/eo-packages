switch operating-system
case 'linux
    shared-library "libSDL3.so"
case 'windows
    shared-library "SDL3.dll"
default
    error "Unsupported OS"

using import Array format slice String include

header := include "SDL3/SDL.h"

vvv bind subscope
do
    using header.define filter "^(SDLK_.+|SDL_WINDOW_.+)$"
    local-scope;

vvv bind incomplete-macros
fold (scope = (Scope)) for k v in subscope
    if (('typeof v) == Symbol)
        if ((v as Symbol) == 'could-not-translate-macro-excess-tokens)
            'bind scope k v
        else scope
    else scope

local macro-gen-code : String
for k v in incomplete-macros
    macro-gen-code ..=
        ..
            format "typeof({k}) ___scopes_macro_constant_{k}() \{ return {k}; \}\n"
                k = (k as Symbol as string)

macro-gen-code ..=
    """"SDL_Thread * SDLCALL ___scopes_macro_fn_CreateThread(SDL_ThreadFunction fn, const char *name, void *data) {
            return SDL_CreateThread(fn, name, data);
        }
        SDL_Thread * SDLCALL ___scopes_macro_fn_CreateThreadWithProperties(SDL_PropertiesID props) {
            return SDL_CreateThreadWithProperties(props);
        }
macro-gen-code as:= string

sugar include* (code)
    code := sc_expand code '() sugar-scope
    qq
        [include] [((.. "#include \"SDL3/SDL.h\" \n" (code as string)) as string)]
            using [header]

run-stage;

header := include* macro-gen-code

vvv bind macro-gen-fns
do
    using header.extern filter "^___scopes_macro_constant_.+$"
    local-scope;

vvv bind header.define
fold (scope = header.define) for k v in macro-gen-fns
    name := Symbol (rslice (k as Symbol as string) (countof str"___scopes_macro_constant_"))
    'bind scope name `(v)

run-stage;

vvv bind sdl
do
    using header.extern filter "^SDL_(.+)$"
    using header.typedef filter "^SDL_(.+)$"
    using header.define filter "^(SDLK?_.+)$"
    using header.const filter "^(SDLK?_.+)"

    using header.extern filter "^___scopes_macro_fn_(.+)$"

    local-scope;

run-stage;

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
augment-enum sdl.Keymod "SDL_KMOD_"
augment-enum sdl.MessageBoxButtonFlags "SDL_MESSAGEBOX_BUTTON_"
augment-enum sdl.MessageBoxFlags "SDL_MESSAGEBOX_"
augment-enum sdl.PowerState "SDL_POWERSTATE_"
augment-enum sdl.Scancode "SDL_SCANCODE_"
augment-enum sdl.WindowFlags "SDL_WINDOW_"
augment-enum sdl.GamepadAxis "SDL_GAMEPAD_AXIS_"
augment-enum sdl.GamepadButton "SDL_GAMEPAD_BUTTON_"
augment-enum sdl.InitFlags "SDL_INIT_"
augment-enum sdl.MessageBoxFlags "SDL_MESSAGEBOX_"

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

result := sdl-macros .. sdl
