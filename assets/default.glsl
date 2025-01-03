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
out float fTexId;

void main() {
    fColor = aColor;
    fTexCoords = aTexCoords;
    fTexId = aTexId;
    gl_Position = uProjection * uView * vec4(aPos, 1.0);
}

#type fragment
#version 330 core

in vec4 fColor;
in vec2 fTexCoords;
in float fTexId;

uniform sampler2D uTexture[8];

out vec4 color;

void main() {
    int id = int(fTexId);
    if(id == 1) {
        color = fColor * texture(uTexture[1], fTexCoords);
    } else if(id == 2) {
        color = fColor * texture(uTexture[2], fTexCoords);
    } else if(id == 3) {
        color = fColor * texture(uTexture[3], fTexCoords);
    } else if(id == 4) {
        color = fColor * texture(uTexture[4], fTexCoords);
    } else if(id == 5) {
        color = fColor * texture(uTexture[5], fTexCoords);
    } else if(id == 6) {
        color = fColor * texture(uTexture[6], fTexCoords);
    } else if(id == 7) {
        color = fColor * texture(uTexture[7], fTexCoords);
    } else {
        color = fColor;
    }

}