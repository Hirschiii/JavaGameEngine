#type vertex
#version 330 core

layout (location = 0) in vec3 aPos;
layout (location = 1) in vec4 aColor;
layout (location = 2) in vec2 aTexCoords;
layout (location = 3) in float aTexId;
layout (location = 4) in float aEntityId;

uniform mat4 uProjection;
uniform mat4 uView;

out vec4 fColor;
out vec2 fTexCoords;
out float fTexId;
out vec2 fPos;

void main(){
	fColor = aColor;
	fTexCoords = aTexCoords;
	fTexId = aTexId;
    fPos = vec2(aPos.x, aPos.y);

	gl_Position = uProjection * uView * vec4(aPos, 1.0);
}

#type fragment
#version 330 core

in vec4 fColor;
in vec2 fPos;
in vec2 fTexCoords;

in float fTexId;
uniform float time;

uniform sampler2D uTextures[8];

out vec4 FragColor;

float rand(vec2 co){
	 return fract(sin(dot(co.xy ,vec2(12.9898,78.233))) * 43758.5453);
}

void main() {
	if (fTexId > 0 ) {
		int id = int(fTexId);
		vec4 color = fColor * texture(uTextures[id], fTexCoords);
		if (color.a > 0.5) {

			// Adding noise
			float noise = rand(fTexCoords + time) * 0.05;

			// Simulate color distortion and slight position shift
			vec2 shift = vec2(rand(fTexCoords + time) * 0.005 - 0.0025, 10.0);
			vec4 rColor = texture(uTextures[id], fPos + shift);
			vec4 gColor = texture(uTextures[id], fPos);
			vec4 bColor = texture(uTextures[id], fPos - shift);

			// Combine color channels with noise
			color.rgb = vec3(rColor.r, gColor.g, bColor.b) + vec3(noise, noise, noise);
			// Implement scan lines

			float scanLine = sin(fPos.y * 800.0 + time) * 0.1; // Adjust the 800.0 for line density and 0.1 for visibility
			color.rgb -= scanLine;
		}

		FragColor = color;
	} else {
		FragColor = fColor;
	}
}
