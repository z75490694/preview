precision mediump float;
uniform sampler2D from, to;
uniform float progress;
uniform vec2 resolution;
varying vec2 vTextureCoord;//接收从顶点着色器过来的参数

void main() {
    vec2 p = gl_FragCoord.xy / resolution.xy;
    if(progress<0.5){
    gl_FragColor = texture2D(from, p); 
	gl_FragColor.a=1.0-progress*2.0;}
	else{
	gl_FragColor = texture2D(to,p); 
	gl_FragColor.a=progress*2.0-1.0;	
	}
}