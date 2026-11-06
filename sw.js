/* Okur & Bütçe çevrimdışı önbelleği (kabuk + Okur + Bütçe & Hedef). Yalnızca kendi önbelleğini siler (aynı adresteki diğer uygulamalara dokunmaz). */
const P='okur-',V=P+'116';
const SHELL=['./','index.html','okur.html','butce.html','sifre.html','manifest.webmanifest','icon.svg','icon-192.png','icon-512.png','icon-maskable.png'];
const SAYFA=['index.html','okur.html','butce.html','sifre.html'];
/* v114: bir dosya inemezse kurulum düşmesin (eskiden addAll tek hatada tümden düşüyor, eski sürüm takılı kalıyordu) */
self.addEventListener('install',e=>{e.waitUntil(caches.open(V).then(c=>Promise.all(SHELL.map(f=>c.add(new Request(f,{cache:'reload'})).catch(()=>{})))).then(()=>self.skipWaiting()))});
/* "Yenile"den sonraki 60 sn sayfalar önbellekten değil, ağdan beklenir (eski sayfa geri gelmesin) */
let taze=0;self.addEventListener('message',e=>{if(e.data==='taze')taze=Date.now()});
self.addEventListener('activate',e=>{e.waitUntil(caches.keys().then(ks=>Promise.all(ks.filter(k=>k.indexOf(P)===0&&k!==V).map(k=>caches.delete(k)))).then(()=>self.clients.claim()))});
self.addEventListener('fetch',e=>{
  const r=e.request;if(r.method!=='GET')return;
  const u=new URL(r.url);
  if(/youtube\.com|ytimg\.com|googlevideo\.com|googleapis\.com/.test(u.hostname))return; /* her zaman ağdan */
  if(/\/ses\/|\/ocr-studyo\/motor\//.test(u.pathname))return; /* büyük ses/OCR dosyaları: uygulama kendi önbelleğinde tutar */
  if(r.mode==='navigate'){
    /* Sayfa adı (kabuk, Okur, Bütçe, Şifre Kasası) kendi adıyla önbelleğe yazılır — çerçeveler birbirinin yerine geçmez */
    const son=u.pathname.split('/').pop(),ad=SAYFA.indexOf(son)>-1?son:'index.html';
    /* Önce ağ (güncelleme gelsin); 3 sn'de yanıt yoksa önbellek: internetsiz açılış takılmaz. Geç gelen yanıt yine önbelleğe yazılır. */
    const net=fetch(r.url,{cache:'no-cache',credentials:'same-origin'}).then(res=>{if(res&&res.ok){const c=res.clone();caches.open(V).then(x=>x.put(ad,c))}return res});
    e.respondWith(Promise.race([net,new Promise((_,rej)=>setTimeout(rej,Date.now()-taze<60000||/[?&]taze=/.test(u.search)?30000:3000))]).catch(()=>caches.match(ad,{cacheName:V}).then(h=>h||net)));
    return;
  }
  e.respondWith(caches.match(r).then(hit=>{
    const net=fetch(r).then(res=>{if(res&&(res.ok||res.type==='opaque')){const c=res.clone();caches.open(V).then(x=>x.put(r,c))}return res}).catch(()=>hit);
    return hit||net;
  }));
});
