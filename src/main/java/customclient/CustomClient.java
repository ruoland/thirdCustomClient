package customclient;


import com.example.ScreenCommand;
import com.example.event.CustomScreenEvent;
import com.example.event.KeyEvent;
import com.example.event.ScreenMouseEvent;
import com.example.event.TitleInitEvent;
import com.mojang.logging.LogUtils;
import customclient.packet.ClientPayloadHandler;
import customclient.packet.MyConfigurationTask;
import customclient.packet.MyData;
import customclient.packet.ServerPayloadHandler;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.network.event.RegisterConfigurationTasksEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(CustomClient.MODID)
public class CustomClient
{// Of course, all mentions of spells can and should be replaced with whatever your registry actually is.
    private static final DeferredRegister<MenuType> MY_MENU = DeferredRegister.create(new ResourceLocation("customclient:mymenu"), "customclient");
    // For some DeferredRegister<MenuType<?>> REGISTER

    // Define mod id in a common place for everything to reference
    public static final String MODID = "customclient";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    public CustomClient(IEventBus modEventBus, ModContainer modContainer)
    {
        NeoForge.EVENT_BUS.addListener(ClientModEvents::registerConfig);
        NeoForge.EVENT_BUS.addListener(ClientModEvents::registerPayload);
        NeoForge.EVENT_BUS.register(new KeyEvent());
        NeoForge.EVENT_BUS.register(new CustomScreenEvent());
        NeoForge.EVENT_BUS.register(new ScreenMouseEvent());
        NeoForge.EVENT_BUS.register(new TitleInitEvent());
        // Register our mod's ModConfigSpec so that FML can create and load the config file for us

        NeoForge.EVENT_BUS.register(this);

    }


    private void commonSetup(final FMLCommonSetupEvent event)
    {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");

        if (ConfigCustomClient.logDirtBlock)
            LOGGER.info("DIRT BLOCK >> {}", BuiltInRegistries.BLOCK.getKey(Blocks.DIRT));

        LOGGER.info(ConfigCustomClient.magicNumberIntroduction + ConfigCustomClient.magicNumber);




    }

    @SubscribeEvent
    public void screenEvent(ScreenEvent.Opening screenEvent){
        if(screenEvent.getNewScreen().getClass().getSimpleName().equals("TitleScreen")){
            GuiMainmenu mainmenu = new GuiMainmenu();
            screenEvent.setNewScreen(mainmenu);
        }
    }

    @SubscribeEvent
    public void screenEvent2(ScreenEvent.Init.Post postInit){
        Screen screen = postInit.getScreen();

        if(screen.getClass().getSimpleName().equals("GuiMainmenu")) {
            CustomClient.LOGGER.info("포스트 클래스 이름 "+screen.getClass().getSimpleName() + "- "+screen.getClass().getSuperclass().getSimpleName());
            OldScreenCustom oldScreenCustom = (OldScreenCustom) screen;
            oldScreenCustom.addButton();
            oldScreenCustom.getData().widgetLoad();

        }
    }


    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        LOGGER.info("HELLO from server starting");
    }

    @SubscribeEvent
    public void registerCommands(RegisterCommandsEvent event){
        ScreenCommand.register(event.getDispatcher());
    }


    @EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void registerPayload(final RegisterPayloadHandlersEvent event) {
            final PayloadRegistrar registrar = event.registrar("1");
            registrar.playBidirectional(
                    MyData.TYPE,
                    MyData.STREAM_CODEC,
                    new DirectionalPayloadHandler<>(
                            ClientPayloadHandler::handleData,
                            ServerPayloadHandler::onMyData
                    )
            );
        }
        @SubscribeEvent
        public static void registerConfig(final RegisterConfigurationTasksEvent event) {
            event.register(new MyConfigurationTask());
        }
    }
}
