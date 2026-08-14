package com.hardware.inventory.config;

import com.hardware.inventory.model.AppUser;
import com.hardware.inventory.model.Category;
import com.hardware.inventory.model.Product;
import com.hardware.inventory.model.Supplier;
import com.hardware.inventory.repository.AppUserRepository;
import com.hardware.inventory.repository.CategoryRepository;
import com.hardware.inventory.repository.ProductRepository;
import com.hardware.inventory.repository.SupplierRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Seeds the database with 150+ realistic computer hardware components.
 * Guarantees at least 15 products for each category.
 */
@Component
public class DataSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);

    private final AppUserRepository userRepo;
    private final CategoryRepository categoryRepo;
    private final ProductRepository productRepo;
    private final SupplierRepository supplierRepo;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(AppUserRepository userRepo,
                      CategoryRepository categoryRepo,
                      ProductRepository productRepo,
                      SupplierRepository supplierRepo,
                      PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.categoryRepo = categoryRepo;
        this.productRepo = productRepo;
        this.supplierRepo = supplierRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (userRepo.count() > 0) {
            log.info("Database already seeded — skipping.");
            return;
        }

        log.info("Seeding database with sample data...");

        // ── Users ────────────────────────────────────────────────
        String encodedPassword = passwordEncoder.encode("admin123");

        AppUser admin = AppUser.builder()
                .username("admin")
                .password(encodedPassword)
                .role("ADMIN")
                .enabled(true)
                .build();

        AppUser user = AppUser.builder()
                .username("user")
                .password(encodedPassword)
                .role("USER")
                .enabled(true)
                .build();

        userRepo.saveAll(List.of(admin, user));
        log.info("Created users: admin (ADMIN), user (USER) — password: admin123");

        // ── Categories ───────────────────────────────────────────
        Category gpu = Category.builder().name("GPU").description("Graphics Cards — NVIDIA RTX & AMD Radeon").build();
        Category cpu = Category.builder().name("CPU").description("Processors — Intel Core Ultra & AMD Ryzen").build();
        Category ram = Category.builder().name("RAM").description("Desktop & Laptop RAM — DDR4 & DDR5").build();
        Category storage = Category.builder().name("Storage").description("NVMe SSD, SATA SSD & Hard Drives").build();
        Category motherboard = Category.builder().name("Motherboard").description("Intel & AMD Motherboards").build();
        Category psu = Category.builder().name("PSU").description("Power Supplies — 80+ Gold & Platinum").build();
        Category cooling = Category.builder().name("Cooling").description("Liquid Coolers, Air Coolers & Fans").build();
        Category pcCase = Category.builder().name("Case").description("Gaming & Workstation PC Chassis").build();
        Category peripheral = Category.builder().name("Peripheral").description("Monitors, Keyboards, Mice & Headsets").build();
        Category networking = Category.builder().name("Networking").description("Wi-Fi 7 Routers, Switches & NICs").build();

        categoryRepo.saveAll(List.of(gpu, cpu, ram, storage, motherboard, psu, cooling, pcCase, peripheral, networking));
        log.info("Created 10 categories");

        // ── Suppliers ────────────────────────────────────────────
        Supplier techDistro = Supplier.builder().name("TechDistro Bangladesh").contactEmail("sales@techdistro.com.bd").phone("+880-1700-55501").build();
        Supplier microParts = Supplier.builder().name("MicroParts BD Ltd").contactEmail("orders@microparts.com.bd").phone("+880-1800-55502").build();
        Supplier siliconWave = Supplier.builder().name("SiliconWave Distro").contactEmail("info@siliconwave.com.bd").phone("+880-1900-55503").build();

        supplierRepo.saveAll(List.of(techDistro, microParts, siliconWave));
        log.info("Created 3 suppliers");

        // ── Products List (15 items per category = 150 products) ─────
        List<Product> productsList = new ArrayList<>();

        // 1. GPU (15 products)
        productsList.add(createPWithImg("NVIDIA GeForce RTX 5090 24GB", "24GB GDDR7, DLSS 4 — Flagship 4K GPU", "239999.00", 12, gpu, "/images/products/rtx5090.png", techDistro, siliconWave));
        productsList.add(createPWithImg("NVIDIA GeForce RTX 5080 16GB", "16GB GDDR7, Next-Gen Ray Tracing", "145000.00", 18, gpu, "/images/products/rtx5080.png", techDistro));
        productsList.add(createPWithImg("NVIDIA GeForce RTX 4090 24GB", "24GB GDDR6X, 384-bit Ultra GPU", "21500.00", 8, gpu, "/images/products/rtx4090.png", siliconWave));
        productsList.add(createPWithImg("ASUS ROG Strix RTX 4080 Super", "16GB GDDR6X OC Edition", "135000.00", 15, gpu, "/images/products/rtx4080s.png", microParts));
        productsList.add(createPWithImg("GIGABYTE RTX 4070 Ti Super", "16GB GDDR6X WINDFORCE OC", "98000.00", 22, gpu, "/images/products/rtx4070tis.png", techDistro));
        productsList.add(createPWithImg("MSI Ventus 3X RTX 4070 Super", "12GB GDDR6X Dual Fan", "78000.00", 30, gpu, "/images/products/rtx4070s_white.png", microParts));
        productsList.add(createPWithImg("ZOTAC Gaming RTX 4060 Ti 16GB", "16GB GDDR6 Twin Edge", "58000.00", 25, gpu, "/images/products/rtx4060ti.png", siliconWave));
        productsList.add(createPWithImg("MSI RTX 4060 Ventus 2X 8GB", "8GB GDDR6 OC Edition", "39500.00", 40, gpu, "/images/products/rtx4060ti.png", techDistro));
        productsList.add(createPWithImg("AMD Radeon RX 7900 XTX 24GB", "24GB GDDR6 RDNA 3 Flagship", "128000.00", 14, gpu, "/images/products/rx7900xtx.png", microParts));
        productsList.add(createPWithImg("AMD Radeon RX 7900 XT 20GB", "20GB GDDR6 High FPS Gaming", "102000.00", 16, gpu, "/images/products/rx7900xtx.png", siliconWave));
        productsList.add(createPWithImg("Sapphire NITRO+ RX 7800 XT", "16GB GDDR6 Tri-X Cooling", "68000.00", 28, gpu, "/images/products/rx7900xtx.png", techDistro));
        productsList.add(createPWithImg("ASRock Challenger RX 7700 XT", "12GB GDDR6 Dual Fan", "56000.00", 20, gpu, "/images/products/rtx4070tis.png", microParts));
        productsList.add(createPWithImg("GIGABYTE RX 7600 XT GAMING OC", "16GB GDDR6 RDNA 3 Architecture", "44000.00", 35, gpu, "/images/products/rtx4070tis.png", siliconWave));
        productsList.add(createPWithImg("Intel Arc B580 12GB Battlemage", "12GB GDDR6 Xe2 Architecture", "36000.00", 15, gpu, "/images/products/gpu_default.png", techDistro));
        productsList.add(createPWithImg("MSI Radeon RX 6600 MECH 2X 8GB", "8GB GDDR6 Budget 1080p GPU", "26500.00", 50, gpu, "/images/products/rtx4060ti.png", microParts));

        // 2. CPU (15 products)
        productsList.add(createPWithImg("Intel Core Ultra 9 285K", "24 Cores / 24 Threads — 5.7 GHz Boost", "70799.00", 25, cpu, "/images/products/cu9_285k.png", siliconWave));
        productsList.add(createPWithImg("Intel Core Ultra 7 265K", "20 Cores / 20 Threads — 5.5 GHz Boost", "49500.00", 30, cpu, "/images/products/cu7_265k.png", techDistro));
        productsList.add(createPWithImg("Intel Core Ultra 5 245K", "14 Cores / 14 Threads — 5.2 GHz Boost", "37500.00", 40, cpu, "/images/products/cu7_265k.png", microParts));
        productsList.add(createPWithImg("Intel Core i9-14900KS", "24 Cores / 32 Threads — 6.2 GHz Special Edition", "82000.00", 6, cpu, "/images/products/cu9_285k.png", siliconWave));
        productsList.add(createPWithImg("Intel Core i7-14700K", "20 Cores / 28 Threads — 5.6 GHz Boost", "48000.00", 35, cpu, "/images/products/cu7_265k.png", techDistro));
        productsList.add(createPWithImg("Intel Core i5-14600K", "14 Cores / 20 Threads — 5.3 GHz Boost", "35000.00", 45, cpu, "/images/products/cu7_265k.png", microParts));
        productsList.add(createPWithImg("Intel Core i5-13400F", "10 Cores / 16 Threads — LGA 1700", "21500.00", 60, cpu, "/images/products/cu7_265k.png", siliconWave));
        productsList.add(createPWithImg("Intel Core i3-14100F", "4 Cores / 8 Threads — 4.7 GHz Budget CPU", "13500.00", 75, cpu, "/images/products/cu7_265k.png", techDistro));
        productsList.add(createPWithImg("AMD Ryzen 9 9950X", "16 Cores / 32 Threads — 5.7 GHz Boost", "65999.00", 20, cpu, "/images/products/ryzen9_9950x.png", techDistro));
        productsList.add(createPWithImg("AMD Ryzen 9 9900X", "12 Cores / 24 Threads — 5.6 GHz Boost", "54000.00", 22, cpu, "/images/products/ryzen9_9950x.png", microParts));
        productsList.add(createPWithImg("AMD Ryzen 7 9700X", "8 Cores / 16 Threads — 5.5 GHz Zen 5", "42000.00", 35, cpu, "/images/products/ryzen9_9950x.png", siliconWave));
        productsList.add(createPWithImg("AMD Ryzen 7 7800X3D", "8 Cores / 16 Threads — 3D V-Cache Gaming King", "49000.00", 18, cpu, "/images/products/ryzen5_7600x.png", techDistro));
        productsList.add(createPWithImg("AMD Ryzen 5 7600X", "6 Cores / 12 Threads — AM5 Architecture", "25500.00", 50, cpu, "/images/products/ryzen5_7600x.png", microParts));
        productsList.add(createPWithImg("AMD Ryzen 5 5600", "6 Cores / 12 Threads — AM4 Value CPU", "14200.00", 80, cpu, "/images/products/ryzen5_7600x.png", siliconWave));
        productsList.add(createPWithImg("AMD Threadripper 7980X", "64 Cores / 128 Threads Workstation Monster", "680000.00", 3, cpu, "/images/products/threadripper_7980x.png", techDistro));

        // 3. RAM (15 products)
        productsList.add(createPWithImg("Corsair Vengeance DDR5 32GB (2x16GB)", "6000MHz CL30 EXPO/XMP Kit", "14999.00", 50, ram, "/images/products/vengeance_ddr5.png", microParts, siliconWave));
        productsList.add(createPWithImg("G.Skill Trident Z5 RGB DDR5 64GB", "6400MHz CL32 Dual Channel Kit", "32000.00", 15, ram, "/images/products/trident_z5_rgb.png", techDistro));
        productsList.add(createPWithImg("Team T-Force Delta RGB DDR5 32GB", "6000MHz CL38 Gaming Memory", "13800.00", 40, ram, "/images/products/tforce_delta_rgb.png", microParts));
        productsList.add(createPWithImg("Kingston FURY Beast DDR5 32GB", "5600MHz CL40 Black Heatsink", "12500.00", 45, ram, "/images/products/fury_beast.png", siliconWave));
        productsList.add(createPWithImg("G.Skill Ripjaws S5 DDR5 32GB", "6000MHz Low Profile RAM", "13200.00", 35, ram, "/images/products/ripjaws_s5.png", techDistro));
        productsList.add(createPWithImg("Corsair Dominator Titanium DDR5 64GB", "7200MHz Ultra Premium Memory", "45000.00", 10, ram, "/images/products/dominator_titanium.png", microParts));
        productsList.add(createPWithImg("Crucial Pro DDR5 32GB (2x16GB)", "5600MHz Plug and Play Desktop RAM", "11800.00", 50, ram, "/images/products/crucial_pro_ddr5.png", siliconWave));
        productsList.add(createPWithImg("ADATA XPG Lancer RGB DDR5 32GB", "6000MHz CL30 Gaming Kit", "14200.00", 30, ram, "/images/products/tforce_delta_rgb.png", techDistro));
        productsList.add(createPWithImg("Corsair Vengeance LPX DDR4 16GB", "3200MHz CL16 Desktop Memory", "4800.00", 100, ram, "/images/products/vengeance_lpx_ddr4.png", microParts));
        productsList.add(createPWithImg("G.Skill Aegis DDR4 16GB", "3200MHz CL16 Budget Module", "4200.00", 90, ram, "/images/products/aegis_ddr4.png", siliconWave));
        productsList.add(createPWithImg("Team Elite DDR4 8GB", "2666MHz Desktop RAM", "2200.00", 120, ram, "/images/products/aegis_ddr4.png", techDistro));
        productsList.add(createPWithImg("Kingston FURY Renegade DDR5 32GB", "7200MHz CL34 RGB Performance", "18500.00", 20, ram, "/images/products/fury_beast.png", microParts));
        productsList.add(createPWithImg("Thermaltake TOUGHRAM XG RGB 32GB", "6000MHz DDR5 Memory", "14500.00", 25, ram, "/images/products/tforce_delta_rgb.png", siliconWave));
        productsList.add(createPWithImg("Patriot Viper Venom DDR5 32GB", "6200MHz High Speed Kit", "13500.00", 30, ram, "/images/products/ripjaws_s5.png", techDistro));
        productsList.add(createPWithImg("PNY XLR8 Gaming Epic-X DDR4 16GB", "3600MHz RGB Desktop RAM", "5200.00", 65, ram, "/images/products/vengeance_lpx_ddr4.png", microParts));

        // 4. Storage (15 products)
        productsList.add(createPWithImg("Samsung 990 Pro 2TB NVMe M.2", "7450 MB/s Read, PCIe 4.0 SSD", "20399.00", 40, storage, "/images/products/kingston_kc3000.png", techDistro, microParts));
        productsList.add(createPWithImg("Samsung 980 Pro 1TB NVMe M.2", "7000 MB/s Read with Heatsink", "12500.00", 55, storage, "/images/products/kingston_kc3000.png", siliconWave));
        productsList.add(createPWithImg("WD Black SN850X 2TB NVMe SSD", "7300 MB/s Gaming M.2 Drive", "19500.00", 35, storage, "/images/products/wd_sn850x.png", techDistro));
        productsList.add(createPWithImg("Crucial T700 2TB PCIe 5.0 NVMe", "12,400 MB/s Next-Gen Speed", "34000.00", 12, storage, "/images/products/crucial_t700.png", microParts));
        productsList.add(createPWithImg("Kingston KC3000 2TB NVMe SSD", "7000 MB/s High Endurance M.2", "16800.00", 45, storage, "/images/products/kingston_kc3000.png", siliconWave));
        productsList.add(createPWithImg("Team Group MP44L 1TB NVMe M.2", "5000 MB/s Gen4 Budget Drive", "7800.00", 80, storage, "/images/products/wd_sn850x.png", techDistro));
        productsList.add(createPWithImg("Lexar NM790 2TB NVMe SSD", "7400 MB/s PCIe 4.0 M.2 Drive", "15500.00", 50, storage, "/images/products/kingston_kc3000.png", microParts));
        productsList.add(createPWithImg("Corsair MP700 PRO 2TB PCIe 5.0", "12,000 MB/s Active Cooler SSD", "36500.00", 8, storage, "/images/products/crucial_t700.png", siliconWave));
        productsList.add(createPWithImg("Solidigm P44 Pro 2TB NVMe SSD", "7000 MB/s Top Tier Gen4 Drive", "18200.00", 30, storage, "/images/products/wd_sn850x.png", techDistro));
        productsList.add(createPWithImg("ADATA XPG GAMMIX S70 Blade 1TB", "7400 MB/s PS5 Compatible SSD", "8900.00", 65, storage, "/images/products/wd_sn850x.png", microParts));
        productsList.add(createPWithImg("Seagate FireCuda 530 2TB NVMe", "7300 MB/s Extreme Performance", "21000.00", 20, storage, "/images/products/kingston_kc3000.png", siliconWave));
        productsList.add(createPWithImg("WD Blue SN580 1TB NVMe M.2", "4150 MB/s Creator SSD", "6900.00", 90, storage, "/images/products/wd_sn850x.png", techDistro));
        productsList.add(createPWithImg("Crucial P3 Plus 1TB Gen4 M.2", "5000 MB/s Everyday NVMe SSD", "7200.00", 85, storage, "/images/products/crucial_t700.png", microParts));
        productsList.add(createPWithImg("Samsung 870 EVO 1TB SATA SSD", "560 MB/s 2.5 Inch Internal Drive", "9500.00", 40, storage, "/images/products/wd_sn850x.png", siliconWave));
        productsList.add(createPWithImg("Seagate IronWolf 8TB NAS HDD", "7200 RPM 256MB Cache SATA 6Gb/s", "24500.00", 25, storage, "/images/products/kingston_kc3000.png", techDistro));

        // 5. Motherboard (15 products)
        productsList.add(createPWithImg("ASUS ROG Maximus Z890 Hero", "Intel Z890 ATX — DDR5, Wi-Fi 7, PCIe 5.0", "75599.00", 10, motherboard, "/images/products/motherboard_default.png", siliconWave));
        productsList.add(createPWithImg("MSI MEG Z890 GODLIKE", "Intel Z890 Flagship E-ATX Board", "145000.00", 4, motherboard, "/images/products/motherboard_default.png", techDistro));
        productsList.add(createPWithImg("GIGABYTE Z890 AORUS Master", "Intel Z890 Gaming Board — Wi-Fi 7", "68000.00", 14, motherboard, "/images/products/motherboard_default.png", microParts));
        productsList.add(createPWithImg("ASUS TUF Gaming Z890-PLUS WiFi", "Intel Z890 ATX Durable Board", "42000.00", 25, motherboard, "/images/products/motherboard_default.png", siliconWave));
        productsList.add(createPWithImg("MSI MAG B760 Tomahawk WiFi", "Intel B760 ATX DDR5 Gaming Board", "26500.00", 40, motherboard, "/images/products/motherboard_default.png", techDistro));
        productsList.add(createPWithImg("GIGABYTE B760M AORUS Elite AX", "Intel B760 Micro-ATX DDR5", "21500.00", 50, motherboard, "/images/products/motherboard_default.png", microParts));
        productsList.add(createPWithImg("ASUS ROG Crosshair X870E Hero", "AMD AM5 X870E Flagship Motherboard", "78000.00", 12, motherboard, "/images/products/motherboard_default.png", siliconWave));
        productsList.add(createPWithImg("GIGABYTE X870E AORUS Master", "AMD AM5 Board — PCIe 5.0, Wi-Fi 7", "65000.00", 18, motherboard, "/images/products/motherboard_default.png", techDistro));
        productsList.add(createPWithImg("MSI MAG X870 Tomahawk WiFi", "AMD AM5 Gaming ATX Motherboard", "38500.00", 30, motherboard, "/images/products/motherboard_default.png", microParts));
        productsList.add(createPWithImg("ASUS TUF Gaming B650-PLUS WiFi", "AMD AM5 Mid-Range ATX Board", "25500.00", 45, motherboard, "/images/products/motherboard_default.png", siliconWave));
        productsList.add(createPWithImg("GIGABYTE B650M Gaming X AX", "AMD AM5 Micro-ATX DDR5 Board", "18500.00", 60, motherboard, "/images/products/motherboard_default.png", techDistro));
        productsList.add(createPWithImg("ASRock B650 Steel Legend WiFi", "AMD AM5 White Theme Board", "24000.00", 22, motherboard, "/images/products/motherboard_default.png", microParts));
        productsList.add(createPWithImg("ASUS PRIME Z790-P WiFi", "Intel Z790 ATX Motherboard", "28000.00", 35, motherboard, "/images/products/motherboard_default.png", siliconWave));
        productsList.add(createPWithImg("MSI PRO B650-P WiFi", "AMD AM5 Business ATX Board", "19500.00", 40, motherboard, "/images/products/motherboard_default.png", techDistro));
        productsList.add(createPWithImg("ASRock Z890 Taichi", "Intel Z890 Premium Gear Theme Board", "62000.00", 15, motherboard, "/images/products/motherboard_default.png", microParts));

        // 6. PSU (15 products)
        productsList.add(createPWithImg("Corsair RM1000x", "1000W 80+ Gold Fully Modular ATX PSU", "22799.00", 35, psu, "/images/products/psu_default.png", techDistro));
        productsList.add(createPWithImg("Seasonic PRIME TX-1300 Titanium", "1300W 80+ Titanium ATX 3.0 PSU", "52000.00", 8, psu, "/images/products/psu_default.png", siliconWave));
        productsList.add(createPWithImg("ASUS ROG Thor 1200W Platinum II", "1200W OLED Display 80+ Platinum", "48000.00", 10, psu, "/images/products/psu_default.png", microParts));
        productsList.add(createPWithImg("MSI MAG A850GL PCIE5 850W", "850W 80+ Gold ATX 3.0 Modular PSU", "14500.00", 50, psu, "/images/products/psu_default.png", techDistro));
        productsList.add(createPWithImg("Thermaltake Toughpower GF3 1000W", "1000W 80+ Gold ATX 3.0 Power Supply", "19800.00", 30, psu, "/images/products/psu_default.png", siliconWave));
        productsList.add(createPWithImg("DeepCool PX1000G WH 1000W", "1000W 80+ Gold White Modular PSU", "18500.00", 25, psu, "/images/products/psu_default.png", microParts));
        productsList.add(createPWithImg("EVGA SuperNOVA 1000 GT", "1000W 80+ Gold Fully Modular PSU", "21000.00", 20, psu, "/images/products/psu_default.png", techDistro));
        productsList.add(createPWithImg("Corsair RM850e 850W", "850W 80+ Gold ATX 3.0 Silent PSU", "15200.00", 60, psu, "/images/products/psu_default.png", siliconWave));
        productsList.add(createPWithImg("Be Quiet! Dark Power 13 1000W", "1000W 80+ Titanium Silent Power", "34000.00", 12, psu, "/images/products/psu_default.png", microParts));
        productsList.add(createPWithImg("Seasonic Focus GX-850", "850W 80+ Gold Fully Modular PSU", "16500.00", 45, psu, "/images/products/psu_default.png", techDistro));
        productsList.add(createPWithImg("Cooler Master MWE Gold 850 V2", "850W 80+ Gold Power Supply", "13200.00", 55, psu, "/images/products/psu_default.png", siliconWave));
        productsList.add(createPWithImg("Antec NE850G M 850W", "850W 80+ Gold Modular PSU", "13800.00", 40, psu, "/images/products/psu_default.png", microParts));
        productsList.add(createPWithImg("Montech TITAN GOLD 1000W", "1000W 80+ Gold ATX 3.0 Power", "17500.00", 30, psu, "/images/products/psu_default.png", techDistro));
        productsList.add(createPWithImg("Corsair CX750M 750W", "750W 80+ Bronze Semi-Modular", "8900.00", 70, psu, "/images/products/psu_default.png", siliconWave));
        productsList.add(createPWithImg("DeepCool PK650D 650W", "650W 80+ Bronze Reliable Power", "5800.00", 85, psu, "/images/products/psu_default.png", microParts));

        // 7. Cooling (15 products)
        productsList.add(createPWithImg("Noctua NH-D15 G2", "Dual-Tower Premium CPU Air Cooler", "13199.00", 45, cooling, "/images/products/case_2.png", microParts));
        productsList.add(createPWithImg("Arctic Liquid Freezer III 360", "360mm High Performance AIO Liquid Cooler", "14800.00", 35, cooling, "/images/products/case_1.png", techDistro));
        productsList.add(createPWithImg("Corsair iCUE LINK H150i LCD", "360mm AIO Liquid Cooler with IPS Screen", "34500.00", 15, cooling, "/images/products/case_1.png", siliconWave));
        productsList.add(createPWithImg("DeepCool LT720 360mm AIO", "360mm Multidimensional Infinity Mirror AIO", "13500.00", 40, cooling, "/images/products/case_1.png", microParts));
        productsList.add(createPWithImg("NZXT Kraken Elite 360 RGB", "360mm AIO with LCD Display & RGB Fans", "36000.00", 12, cooling, "/images/products/case_1.png", techDistro));
        productsList.add(createPWithImg("Thermalright Peerless Assassin 120 SE", "Dual-Tower Budget King Air Cooler", "4500.00", 120, cooling, "/images/products/case_2.png", siliconWave));
        productsList.add(createPWithImg("Be Quiet! Dark Rock Pro 5", "Dual-Tower Ultra Silent CPU Cooler", "11800.00", 25, cooling, "/images/products/case_2.png", microParts));
        productsList.add(createPWithImg("ASUS ROG Ryujin III 360 ARGB", "360mm AIO with 3.5 Inch LCD Screen", "42000.00", 8, cooling, "/images/products/case_1.png", techDistro));
        productsList.add(createPWithImg("Lian Li Galahad II Trinity 360", "360mm High Airflow ARGB AIO", "16500.00", 30, cooling, "/images/products/case_1.png", siliconWave));
        productsList.add(createPWithImg("Cooler Master MasterLiquid 360L", "360mm Core ARGB Liquid Cooler", "10500.00", 50, cooling, "/images/products/case_1.png", microParts));
        productsList.add(createPWithImg("EK-Nucleus AIO CR360 Lux Digital", "360mm Custom Loop Quality AIO", "21500.00", 18, cooling, "/images/products/case_1.png", techDistro));
        productsList.add(createPWithImg("Thermalright Phantom Spirit 120 EVO", "7 Heatpipe Premium CPU Air Cooler", "5800.00", 75, cooling, "/images/products/case_2.png", siliconWave));
        productsList.add(createPWithImg("DeepCool AK620 Digital", "Dual-Tower Air Cooler with Temp Screen", "8200.00", 60, cooling, "/images/products/case_2.png", microParts));
        productsList.add(createPWithImg("Noctua NF-A12x25 PWM 120mm Fan", "Premium Quiet 120mm Case Fan", "3400.00", 150, cooling, "/images/products/case_2.png", techDistro));
        productsList.add(createPWithImg("Arctic P12 PWM PST 5-Pack Fans", "5-Pack Value 120mm Pressure Fans", "4200.00", 90, cooling, "/images/products/case_2.png", siliconWave));

        // 8. Case (15 products)
        productsList.add(createPWithImg("Lian Li O11 Dynamic EVO", "Mid-Tower ATX Case — Tempered Glass", "20399.00", 20, pcCase, "/images/products/case_1.png", siliconWave));
        productsList.add(createPWithImg("Lian Li O11 Dynamic EVO XL", "Full-Tower Modular Glass Gaming Case", "28500.00", 14, pcCase, "/images/products/case_3.png", techDistro));
        productsList.add(createPWithImg("HYTE Y70 Touch Red/Black", "Dual-Chamber Case with 4K Touch Screen", "46000.00", 6, pcCase, "/images/products/case_1.png", microParts));
        productsList.add(createPWithImg("NZXT H9 Flow White", "Dual-Chamber Panoramic Glass Case", "21500.00", 22, pcCase, "/images/products/case_1.png", siliconWave));
        productsList.add(createPWithImg("Corsair 5000D AIRFLOW", "Mid-Tower ATX High-Airflow Case", "18500.00", 30, pcCase, "/images/products/case_3.png", techDistro));
        productsList.add(createPWithImg("Fractal Design North XL Walnut", "Chassis with Real Walnut Wood Front", "24500.00", 16, pcCase, "/images/products/case_2.png", microParts));
        productsList.add(createPWithImg("Phanteks NV7 Black", "Showcase Full-Tower Glass Chassis", "26000.00", 12, pcCase, "/images/products/case_1.png", siliconWave));
        productsList.add(createPWithImg("Montech KING 95 PRO Prussian Blue", "Curved Glass Panoramic Gaming Case", "17800.00", 25, pcCase, "/images/products/case_2.png", techDistro));
        productsList.add(createPWithImg("Be Quiet! Shadow Base 800 FX", "Full-Tower ARGB Silent Chassis", "23000.00", 15, pcCase, "/images/products/case_3.png", microParts));
        productsList.add(createPWithImg("Cooler Master MasterBox TD500 Mesh", "3D Polygon Mesh ARGB Case", "11500.00", 40, pcCase, "/images/products/case_2.png", siliconWave));
        productsList.add(createPWithImg("Lian Li Lancool 216 RGB", "High-Airflow Mesh Mid-Tower Case", "12800.00", 45, pcCase, "/images/products/case_2.png", techDistro));
        productsList.add(createPWithImg("Thermaltake Tower 500 Vertical Case", "Vertical Showcase Mid-Tower", "19500.00", 18, pcCase, "/images/products/case_3.png", microParts));
        productsList.add(createPWithImg("Antec Performance 1 FT", "Full-Tower E-ATX High Performance Case", "19800.00", 20, pcCase, "/images/products/case_3.png", siliconWave));
        productsList.add(createPWithImg("DeepCool CH560 Digital", "Mesh Case with CPU/GPU Temp Display", "12500.00", 35, pcCase, "/images/products/case_2.png", techDistro));
        productsList.add(createPWithImg("NZXT H6 Flow RGB Black", "Compact Dual-Chamber Glass Chassis", "16500.00", 28, pcCase, "/images/products/case_1.png", microParts));

        // 9. Peripheral (15 products)
        productsList.add(createPWithImg("Logitech G Pro X Superlight 2", "LIGHTSPEED Wireless Gaming Mouse", "17500.00", 40, peripheral, "/images/products/mouse_default.png", techDistro));
        productsList.add(createPWithImg("Razer DeathAdder V3 Pro", "Ultra-Lightweight Ergonomic Wireless Mouse", "16800.00", 35, peripheral, "/images/products/mouse_default.png", microParts));
        productsList.add(createPWithImg("SteelSeries Apex Pro TKL Wireless", "OmniPoint Adjustable Switch Keyboard", "28500.00", 15, peripheral, "/images/products/keyboard.png", siliconWave));
        productsList.add(createPWithImg("Corsair K100 RGB Mechanical", "OPX Optical-Mechanical Gaming Keyboard", "26000.00", 18, peripheral, "/images/products/keyboard.png", techDistro));
        productsList.add(createPWithImg("Wooting 60HE+ Analog Keyboard", "Rapid Trigger Hall Effect Gaming Keyboard", "27500.00", 12, peripheral, "/images/products/keyboard.png", microParts));
        productsList.add(createPWithImg("ASUS ROG Swift PG32UCDM 4K OLED", "32 Inch 4K 240Hz QD-OLED Gaming Monitor", "165000.00", 5, peripheral, "/images/products/monitor.png", siliconWave));
        productsList.add(createPWithImg("Alienware AW2725DF 360Hz OLED", "27 Inch QHD 360Hz QD-OLED Gaming Monitor", "118000.00", 8, peripheral, "/images/products/monitor.png", techDistro));
        productsList.add(createPWithImg("LG UltraGear 27GR95QE OLED", "27 Inch QHD 240Hz 0.03ms OLED Monitor", "98000.00", 10, peripheral, "/images/products/monitor.png", microParts));
        productsList.add(createPWithImg("HyperX Cloud III Wireless", "Up to 120hr Battery Life Gaming Headset", "16500.00", 45, peripheral, "/images/products/keyboard.png", siliconWave));
        productsList.add(createPWithImg("Razer BlackShark V2 Pro 2023", "Esports Wireless Gaming Headset", "19500.00", 30, peripheral, "/images/products/keyboard.png", techDistro));
        productsList.add(createPWithImg("Logitech G915 LIGHTSPEED Wireless", "Low Profile Mechanical Gaming Keyboard", "23500.00", 20, peripheral, "/images/products/keyboard.png", microParts));
        productsList.add(createPWithImg("EPOMAKER EK68 Gasket Mechanical", "65% Hot-Swappable Wireless Keyboard", "8900.00", 60, peripheral, "/images/products/keyboard.png", siliconWave));
        productsList.add(createPWithImg("Finalmouse UltralightX Lion", "Carbon Fiber Composite 31g Mouse", "29500.00", 7, peripheral, "/images/products/mouse_default.png", techDistro));
        productsList.add(createPWithImg("Elgato Stream Deck MK.2", "15 Customizable LCD Macro Keys", "18500.00", 25, peripheral, "/images/products/keyboard.png", microParts));
        productsList.add(createPWithImg("Shure SM7B Vocal Microphone", "Dynamic Cardioid Studio Microphone", "46000.00", 14, peripheral, "/images/products/keyboard.png", siliconWave));

        // 10. Networking (15 products)
        productsList.add(createPWithImg("ASUS ROG Rapture GT-BE98 Pro", "Quad-Band Wi-Fi 7 Gaming Router", "95000.00", 6, networking, "/images/products/wifi_card.png", techDistro));
        productsList.add(createPWithImg("TP-Link Archer BE800 Wi-Fi 7", "Tri-Band 19Gbps Wi-Fi 7 Router", "68000.00", 10, networking, "/images/products/wifi_card.png", microParts));
        productsList.add(createPWithImg("Netgear Nighthawk RAXE500", "Tri-Band Wi-Fi 6E Router 10.8Gbps", "62000.00", 12, networking, "/images/products/wifi_card.png", siliconWave));
        productsList.add(createPWithImg("Ubiquiti UniFi Dream Router", "All-in-One Console with Wi-Fi 6", "28500.00", 18, networking, "/images/products/wifi_card.png", techDistro));
        productsList.add(createPWithImg("TP-Link Deco XE75 Mesh (3-Pack)", "Tri-Band AXE5400 Whole Home Mesh", "38000.00", 22, networking, "/images/products/wifi_card.png", microParts));
        productsList.add(createPWithImg("ASUS RT-AX88U Pro Dual Band", "AX6000 Wi-Fi 6 Router with 2.5G Ports", "34000.00", 25, networking, "/images/products/wifi_card.png", siliconWave));
        productsList.add(createPWithImg("Linksys Hydra Pro 6E Tri-Band", "AXE6600 Wi-Fi 6E Mesh Router", "29500.00", 20, networking, "/images/products/wifi_card.png", techDistro));
        productsList.add(createPWithImg("D-Link DGS-108 8-Port Switch", "8-Port Gigabit Unmanaged Metal Switch", "2800.00", 100, networking, "/images/products/network_switch.png", microParts));
        productsList.add(createPWithImg("Intel Wi-Fi 7 BE200 PCIe Card", "Wi-Fi 7 + Bluetooth 5.4 Desktop Card", "4800.00", 75, networking, "/images/products/wifi_card.png", siliconWave));
        productsList.add(createPWithImg("TP-Link TX401 10G PCIe NIC", "10 Gigabit RJ45 Network Adapter", "9500.00", 35, networking, "/images/products/nic_card.png", techDistro));
        productsList.add(createPWithImg("MikroTik hEX S Gigabit Router", "5-Port RouterBoard with SFP Slot", "8200.00", 40, networking, "/images/products/network_switch.png", microParts));
        productsList.add(createPWithImg("Netgear GS308P PoE Switch", "8-Port Gigabit Switch with 4-Port PoE", "8500.00", 30, networking, "/images/products/network_switch.png", siliconWave));
        productsList.add(createPWithImg("Cisco CBS250-24T-4G Switch", "24-Port Gigabit Smart Managed Switch", "48000.00", 8, networking, "/images/products/network_switch.png", techDistro));
        productsList.add(createPWithImg("QNAP QSW-2104-2S 10GbE Switch", "2-Port 10GbE SFP+ & 4-Port 2.5GbE", "21500.00", 15, networking, "/images/products/network_switch.png", microParts));
        productsList.add(createPWithImg("Wavlink AC1200 Range Extender", "Dual Band High Power Outdoor AP/Repeater", "6500.00", 50, networking, "/images/products/wifi_card.png", siliconWave));

        // Save all 150 products
        productRepo.saveAll(productsList);
        log.info("Created {} products total (15 per category)", productsList.size());

        log.info("Database seeding complete!");
    }

    private Product createP(String name, String desc, String price, int stock, Category cat, Supplier... suppliers) {
        return createPWithImg(name, desc, price, stock, cat, null, suppliers);
    }

    private Product createPWithImg(String name, String desc, String price, int stock, Category cat, String imgUrl, Supplier... suppliers) {
        return Product.builder()
                .name(name)
                .description(desc)
                .price(new BigDecimal(price))
                .stockQuantity(stock)
                .category(cat)
                .imageUrl(imgUrl)
                .suppliers(Set.of(suppliers))
                .build();
    }
}
