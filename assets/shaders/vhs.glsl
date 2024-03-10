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

void main(){
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
uniform float time;

vec4 color;

uniform sampler2D uTextures[8];

out vec4 FragColor;

float rand(vec2 co){
	 return fract(sin(dot(co.xy ,vec2(12.9898,78.233))) * 43758.5453);
}

void main() {
	if (fTexId > 0 ) {
		int id = int(fTexId);
        switch (id) {
            case 0:
                color = fColor * texture(uTextures[0], fTexCoords);
            break;
            case 1:
                color = fColor * texture(uTextures[1], fTexCoords);
            break;
            case 2:
                color = fColor * texture(uTextures[2], fTexCoords);
            break;
            case 3:
                color = fColor * texture(uTextures[3], fTexCoords);
            break;
            case 4:
                color = fColor * texture(uTextures[4], fTexCoords);
            break;
            case 5:
                color = fColor * texture(uTextures[5], fTexCoords);
            break;
            case 6:
                color = fColor * texture(uTextures[6], fTexCoords);
            break;
            case 7:
                color = fColor * texture(uTextures[7], fTexCoords);
            break;
		}
		if (color.a > 0.5) {

			// Adding noise
			float noise = rand(fTexCoords + time) * 0.05;

			// Simulate color distortion and slight position shift
			vec2 shift = vec2(rand(fTexCoords + time) * 0.005 - 0.0025, 10.0);

			vec4 rColor;
			vec4 gColor;
			vec4 bColor; 


			switch (id) {
				case 0:
					rColor = texture(uTextures[0], fTexCoords + shift);
					gColor = texture(uTextures[0], fTexCoords);
					bColor = texture(uTextures[0], fTexCoords - shift);
				break;
				case 1:
					rColor = texture(uTextures[1], fTexCoords + shift);
					gColor = texture(uTextures[1], fTexCoords);
					bColor = texture(uTextures[1], fTexCoords - shift);
				break;
				case 2:
					rColor = texture(uTextures[2], fTexCoords + shift);
					gColor = texture(uTextures[2], fTexCoords);
					bColor = texture(uTextures[2], fTexCoords - shift);
				break;
				case 3:
					rColor = texture(uTextures[3], fTexCoords + shift);
					gColor = texture(uTextures[3], fTexCoords);
					bColor = texture(uTextures[3], fTexCoords - shift);
				break;
				case 4:
					rColor = texture(uTextures[4], fTexCoords + shift);
					gColor = texture(uTextures[4], fTexCoords);
					bColor = texture(uTextures[4], fTexCoords - shift);
				break;
				case 5:
					rColor = texture(uTextures[5], fTexCoords + shift);
					gColor = texture(uTextures[5], fTexCoords);
					bColor = texture(uTextures[5], fTexCoords - shift);
				break;
				case 6:
					rColor = texture(uTextures[6], fTexCoords + shift);
					gColor = texture(uTextures[6], fTexCoords);
					bColor = texture(uTextures[6], fTexCoords - shift);
				break;
				case 7:
					rColor = texture(uTextures[7], fTexCoords + shift);
					gColor = texture(uTextures[7], fTexCoords);
					bColor = texture(uTextures[7], fTexCoords - shift);
				break;
			}

			// Combine color channels with noise
			color.rgb = vec3(rColor.r, gColor.g, bColor.b) + vec3(noise, noise, noise);
			// Implement scan lines

			float scanLine = sin(fTexCoords.y * 800.0) * 0.1; // Adjust the 800.0 for line density and 0.1 for visibility
			color.rgb -= scanLine;
		}

		FragColor = color;
	} else {
		FragColor = fColor;
	}
}
