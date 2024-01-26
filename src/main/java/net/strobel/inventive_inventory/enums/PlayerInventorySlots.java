package net.strobel.inventive_inventory.enums;

import java.util.stream.IntStream;

public enum PlayerInventorySlots {
    ARMOR {
        private final int[] slots = IntStream.range(5, 9).toArray();
        @Override
        public int[] getSlots() {
            return slots;
        }

        @Override
        public int getFirstSlot() {
            return slots[0];
        }

        @Override
        public int getLastSlot() {
            return slots[slots.length - 1];
        }
    },
    UPPER_INVENTORY {
        private final int[] slots = IntStream.range(9, 36).toArray();
        @Override
        public int[] getSlots() {
            return slots;
        }

        @Override
        public int getFirstSlot() {
            return slots[0];
        }

        @Override
        public int getLastSlot() {
            return slots[slots.length - 1];
        }
    },
    HOTBAR {
        private final int[] slots = IntStream.range(36, 45).toArray();
        @Override
        public int[] getSlots() {
            return slots;
        }

        @Override
        public int getFirstSlot() {
            return slots[0];
        }

        @Override
        public int getLastSlot() {
            return slots[slots.length - 1];
        }
    },
    OFFHAND {
        private final int[] slots = IntStream.of(45).toArray();
        @Override
        public int[] getSlots() {
            return slots;
        }

        @Override
        public int getFirstSlot() {
            return slots[0];
        }

        @Override
        public int getLastSlot() {
            return slots[slots.length - 1];
        }
    },
    FULL_INVENTORY {
        private final int[] slots = IntStream.range(9, 46).toArray();
        @Override
        public int[] getSlots() {
            return slots;
        }

        @Override
        public int getFirstSlot() {
            return slots[0];
        }

        @Override
        public int getLastSlot() {
            return slots[slots.length - 1];
        }
    };

    public abstract int[] getSlots();
    public abstract int getFirstSlot();
    public abstract int getLastSlot();
}