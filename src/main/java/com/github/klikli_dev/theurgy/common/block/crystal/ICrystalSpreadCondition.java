/*
 * MIT License
 *
 * Copyright 2020 klikli-dev
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT
 * OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */

package com.github.klikli_dev.theurgy.common.block.crystal;

import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public interface ICrystalSpreadCondition {
    /**
     * Checks if a crystal can spread to the given position.
     * @param world the world to spread in.
     * @param targetState the block state of the target position.
     * @param targetPos the target position.
     * @return true if can spread, false otherwise
     */
    boolean canSpreadTo(IWorld world, BlockState targetState, BlockPos targetPos);

    /**
     * Gets a list of valid placement directions. Can be used to restrict spread to e.g. ceilings only.
     * @return a list of directions.
     */
    default List<Direction> getValidPlacementDirections(){
        return Arrays.asList(Direction.values());
    }

    /**
     * Gets a random valid placement direction for the target block pos.
     * @param world the world to place in.
     * @param targetPos the target block pos.
     * @return The direction to place or null if there is no valid one.
     */
    default Direction getPlacementDirection(IWorld world, BlockPos targetPos){
        List<Direction> directions = this.getValidPlacementDirections();
        Collections.shuffle(directions);
        for(Direction direction : directions){
            BlockState neighbor = world.getBlockState(targetPos.offset(direction));
            if(neighbor.isSolid())
                return direction;
        }

        return null;
    }
}
