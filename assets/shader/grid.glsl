#type vertex
#version 330 core

layout(location = 0) in vec3 aPos;
layout(location = 1) in vec4 aColor;
layout(location = 2) in vec2 aTexCoords;
layout(location = 3) in float aTexId;

uniform mat4 uProjection;
uniform mat4 uView;

out vec4 fColor;
out vec2 fTexCoords;
out vec2 fPos;
out float fTexId;

void main() {
    fColor = aColor;
    fPos=aPos.xy;
    gl_Position = uProjection * uView * vec4(aPos, 1.0);
}

#type fragment
#version 330 core

in vec2 fPos;
in vec4 fColor;

uniform sampler2D uTexture[8];

out vec4 color;

vec2 pitch  = vec2(50., 50.);

void main() {    
    if (mod(fPos.x, pitch[0]) < 2. ||
        mod(fPos.y, pitch[1]) < 2.) {
        color = vec4(0.2275, 0.2118, 0.2118, 1.0);
    } else {
        color=fColor;
    }
}