package com.globbypotato.rockhounding_chemistry.machines.tileentity;

import com.globbypotato.rockhounding_chemistry.handlers.Reference;
import com.globbypotato.rockhounding_chemistry.machines.container.ContainerOwcController;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityLockable;
import net.minecraft.util.ITickable;

public abstract class TileEntityOwcBaseController extends TileEntityLockable implements ITickable {
    private String inventoryName;
    
    public ItemStack[] slots = new ItemStack[2];

	@Override
	public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
		return new ContainerOwcController(playerInventory, (TileEntityOwcController) this);
	}

	@Override
	public String getGuiID() {
        return Reference.MODID + ":owcController";
	}

    public String getName(){
        return this.hasCustomName() ? this.inventoryName : "container.owcController";
    }

    public boolean hasCustomName(){
        return this.inventoryName != null && !this.inventoryName.isEmpty();
    }

    public void setCustomInventoryName(String name){
        this.inventoryName = name;
    }

	@Override
	public int getSizeInventory() {
        return this.slots.length;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
        return this.slots[index];
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
        return ItemStackHelper.getAndSplit(this.slots, index, count);
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
        return ItemStackHelper.getAndRemove(this.slots, index);
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
        boolean flag = stack != null && stack.isItemEqual(this.slots[index]) && ItemStack.areItemStackTagsEqual(stack, this.slots[index]);
        this.slots[index] = stack;
        if (stack != null && stack.stackSize > this.getInventoryStackLimit()){
            stack.stackSize = this.getInventoryStackLimit();
        }
        if (index == 0 && !flag){
            this.markDirty();
        }
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
        return this.worldObj.getTileEntity(this.pos) != this ? false : player.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
	}

	@Override
	public void openInventory(EntityPlayer player) {	}

	@Override
	public void closeInventory(EntityPlayer player) {	}

	@Override
	public void clear() {        
		for (int i = 0; i < this.slots.length; ++i) {
			this.slots[i] = null;
		}
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return true;
	}

    //----------------------- I/O -----------------------
    @Override
    public void readFromNBT(NBTTagCompound compound){
        super.readFromNBT(compound);
        if (compound.hasKey("CustomName", 8)){
            this.inventoryName = compound.getString("CustomName");
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound){
        super.writeToNBT(compound);
        if (this.hasCustomName()){
            compound.setString("CustomName", this.inventoryName);
        }
        return compound;
    }

	@Override
	public void update() {
	}

}