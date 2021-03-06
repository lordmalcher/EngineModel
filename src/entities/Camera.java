package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

public class Camera {
    private static final float MIN_DISTANCE = 2;
    private static final float INIT_PITCH = 20;
    private final Vector3f position = new Vector3f(0,0,0);
    private final FocusPoint focusPoint;
    private float pitch = INIT_PITCH;
    private float yaw = 0;
    private float distanceFromFocusPoint = 0;

    public Camera(FocusPoint focusPoint) {
        this.focusPoint = focusPoint;
    }

    public void move() {
        focusPoint.move();
        calculateZoom();
        calculateRotations();
        float horizontalDistance = calculateHorizontalDistance();
        float verticalDistance = calculateVerticalDistance();
        calculateCameraPosition(horizontalDistance, verticalDistance);
//        this.yaw = focusPoint.getRotation();
    }

    public Vector3f getPosition() {
        return position;
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }

    private void calculateCameraPosition(float horizontalDistance, float verticalDistance) {
        float theta = focusPoint.getRotation();
        float offsetX = (float) (horizontalDistance * Math.sin(Math.toRadians(theta)));
        float offsetZ = (float) (horizontalDistance * Math.cos(Math.toRadians(theta)));
        position.x = focusPoint.getPosition().x - offsetX;
        position.y = focusPoint.getPosition().y + verticalDistance;
        position.z = focusPoint.getPosition().z + offsetZ;
    }

    private float calculateHorizontalDistance() {
        return (float) (distanceFromFocusPoint * Math.cos(Math.toRadians(pitch)));
    }

    private float calculateVerticalDistance() {
        return (float) (distanceFromFocusPoint * Math.sin(Math.toRadians(pitch)));
    }

    private void calculateZoom() {
        float zoomLevel = Mouse.getDWheel() * 0.1f;
        if (distanceFromFocusPoint - zoomLevel >= MIN_DISTANCE) {
            distanceFromFocusPoint -= zoomLevel;
        } else {
            distanceFromFocusPoint = MIN_DISTANCE;
        }
    }

    private void calculateRotations() {
        if (Keyboard.isKeyDown(Keyboard.KEY_R)) {
            pitch = INIT_PITCH;
        }
        if (Mouse.isButtonDown(0)) {
            float pitchChange = Mouse.getDY() * 0.1f;
            pitch += pitchChange;
            if (pitch > 90) {
                pitch = 90;
            } else if (pitch < -90) {
                pitch = -90;
            }
        }
        if (Mouse.isButtonDown(0)) {
            float yawChange = Mouse.getDX() * 0.1f;
            yaw -= yawChange;
        }
    }
}

