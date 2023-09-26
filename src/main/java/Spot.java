import org.joml.Matrix4f;
import org.joml.Vector4f;

public class Spot {
	
	private Vector4f pos;
	private float size;
	
	public Spot(Vector4f pos, float size) {
		this.pos = pos;
		this.size = size;
	}
	
	public void render(Matrix4f viewProjection, float aspect) {
		Vector4f p = viewProjection.transform(pos, new Vector4f());
		
		if (p.z < 0) {
			return;
		}
		p.div(p.w);
		StdDraw.ellipse(p.x + 0.5, p.y + 0.5, size, size * aspect);
	}
}
