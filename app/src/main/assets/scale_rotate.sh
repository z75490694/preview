precision mediump float;
uniform sampler2D from, to;
uniform float progress;
uniform vec2 resolution;
varying vec2 vTextureCoord;  //用于传递给片元着色器的变量
const float strength=0.1;

void main() {
    vec2 p = gl_FragCoord.xy / resolution.xy;
  
        
    if(progress<0.99){
	gl_FragColor =texture2D(from, p);
	gl_FragColor.a=1.0-progress;	
	}
	else{
    gl_FragColor =texture2D(to, vTextureCoord);
	}
}      