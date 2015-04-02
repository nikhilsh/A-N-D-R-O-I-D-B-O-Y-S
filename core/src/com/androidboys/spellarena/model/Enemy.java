package com.androidboys.spellarena.model;

public class Enemy extends Bob{

	private float fx;
	private float fy;

	public Enemy(int i, int j) {
		super(i,j);
	}

	@Override
	public void update(float delta){
		super.checkCollision(delta);
		updateVelocity(delta);
		super.updateState();
	}
	
	private void updateVelocity(float delta) {
		float xdist = fx - this.getPosition().x;
		float ydist = fy - this.getPosition().y;
		this.setVelocity(xdist/delta, ydist/delta);
	}

	@Override
	protected void updateVelocity(){
		if(this.velocity.x > 0){
			if(this.velocity.y > 0){
				this.direction = Direction.NORTHEAST;
			} else if (this.velocity.y < 0){
				this.direction = Direction.SOUTHEAST;
			} else {
				this.direction = Direction.EAST;
			}
		} else if (this.velocity.x < 0){
			if(this.velocity.y > 0){
				this.direction = Direction.NORTHWEST;
			} else if (this.velocity.y < 0){
				this.direction = Direction.SOUTHWEST;
			} else {
				this.direction = Direction.WEST;
			}
		} else {
			if(this.velocity.y > 0){
				this.direction = Direction.NORTH;
			} else if (this.velocity.y < 0){
				this.direction = Direction.SOUTH;
			}
		}
	}
	
	public void setFuturePosition(float x, float y) {
		this.fx = x;
		this.fy = y;
	}
}
