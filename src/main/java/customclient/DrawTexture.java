package customclient;

import net.minecraft.resources.ResourceLocation;

public class DrawTexture extends Widget {
    private transient ResourceLocation TEXTURE;
    private String fileName;
    DrawTexture(String fileName, ResourceLocation resourceLocation){
        super(new FakeTextureWidget());
        this.TEXTURE = resourceLocation;
        this.fileName = fileName;
    }

    public void setTexture(ResourceLocation texture) {
        this.TEXTURE = TEXTURE;
    }

    public ResourceLocation getTexture() {
        if(TEXTURE == null)
            TEXTURE = new ResourceLocation("customclient", fileName);

        return TEXTURE;
    }

    @Override
    public void update() {
        super.update();


    }
}
