package team168;


import battlecode.common.*;

public class Soldier extends RobotPlayer {

    protected static MapLocation order = null;

    protected static void playTurn() {
        Signal[] signals = rc.emptySignalQueue();

        Signal[] alliedComplexSignalsOnly = getAlliedComplexSignalsOnly(signals);
        for (int i = 0; i < alliedComplexSignalsOnly.length; i++) {
            int[] message = alliedComplexSignalsOnly[i].getMessage();
            if (message[0] == LEADER_COMMAND) {
                if (message[1] == MUSTER_AT_LOCATION) {
                    i++;
                    message = alliedComplexSignalsOnly[i].getMessage();

                    order = new MapLocation(message[0], message[1]);
                }
            }
        }

        if (order != null) {
            if (!rc.canAttackLocation(order)) {
                makeBestFirstMoveAndClearRubble(myLocation.directionTo(order));
            }
            Direction direction = Direction.values()[rc.getID() % 8];
            makeBestFirstMoveAndClearRubble(myLocation.directionTo(order.add(direction, 3)));
        }

        // TODO TEST
        RobotInfo[] enemiesInRange = getAllHostilesWithinRange(attackRadius);
        if (enemiesInRange.length > 0) {
            if (rc.isWeaponReady()) {
                try {
                    rc.attackLocation(getLocationPercentageOfRobotWithLowestHP(enemiesInRange));
                } catch (GameActionException e) {
                    e.printStackTrace();
                }
            }
        }

       /* RobotInfo[] enemiesInSensorRange = getAllHostilesWithinRange(mySensorRadius);
        if (enemiesInSensorRange.length > 0) {
            rc.setIndicatorString(0, "Enemies detected " +enemiesInSensorRange.length );
            makeBestFirstMoveAndClearRubble(myLocation.directionTo(enemiesInSensorRange[0].location));
        }*/
    }
}
