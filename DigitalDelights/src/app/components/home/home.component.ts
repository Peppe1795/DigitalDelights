import {
  Component,
  OnInit,
  AfterViewInit,
  ElementRef,
  ViewChild,
  ViewChildren,
  QueryList,
} from '@angular/core';
import { ProductsService } from 'src/app/services/products.service';
import { Product } from 'src/app/models/products';
import { gsap } from 'gsap';
import { ScrollTrigger } from 'gsap/ScrollTrigger';

gsap.registerPlugin(ScrollTrigger);

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent implements OnInit, AfterViewInit {
  products: Product[] = [];
  randomProducts: Product[] = [];

  @ViewChild('iphone', { static: false }) iphone!: ElementRef;
  @ViewChildren('widgets', { read: ElementRef })
  widgetElements!: QueryList<ElementRef>;

  constructor(private productService: ProductsService) {}

  ngOnInit(): void {
    this.productService.getProducts(1, 'someOrder').subscribe(
      (data) => {
        this.products = data;
        this.randomProducts = this.selectRandomProducts(this.products, 8);
      },
      (error) => {
        console.error('Errore nel recupero dei prodotti', error);
      }
    );
  }

  ngAfterViewInit(): void {
    this.initializeAnimations();
  }

  selectRandomProducts(products: Product[], count: number): Product[] {
    const shuffled = products.sort(() => 0.5 - Math.random());
    return shuffled.slice(0, count);
  }

  initializeAnimations() {
    if (!this.iphone) {
      console.error('iPhone element not yet initialized');
      return;
    }

    const allWidgets = this.widgetElements
      .toArray()
      .map((widget) => widget.nativeElement);

    gsap.set(this.iphone.nativeElement, { x: -450, rotation: 90 });
    gsap.set(allWidgets, { opacity: 0, scale: 0 });

    const masterTimeline = gsap.timeline();
    masterTimeline.add(this.iPhoneAnimation()).add(this.widgetAnimation(), 2);

    animations.forEach((animation, index) => {
      const { selector, duration, scale, x, y, ease } = animation;
      const element = this.iphone.nativeElement.querySelector(selector);
      masterTimeline.add(
        gsap.to(element, { duration, scale, x, y, ease }),
        2 + (index % 3) / 2
      );
    });

    ScrollTrigger.create({
      animation: masterTimeline,
      trigger: '.animation',
      scrub: 1,
      pin: true,
    });
  }

  iPhoneAnimation() {
    const tl = gsap.timeline({ defaults: { duration: 1 } });
    tl.to(this.iphone.nativeElement, { x: 0 })
      .to(this.iphone.nativeElement, { rotation: 0, scale: 0.9 })
      .to(this.iphone.nativeElement, { duration: 3, scale: 1 });
    return tl;
  }

  widgetAnimation() {
    const tl = gsap.timeline();

    this.widgetElements.toArray().forEach((widget, index) => {
      const anim = animations.find(
        (a) => a.selector === `#${widget.nativeElement.id}`
      );
      if (anim) {
        tl.to(
          widget.nativeElement,
          {
            duration: anim.duration,
            scale: anim.scale,
            x: anim.x,
            y: anim.y,
            ease: anim.ease,
            opacity: 1,
          },
          index * 0.5
        );
      }
    });

    return tl;
  }
}

const animations = [
  {
    selector: '#app-store',
    duration: 3,
    scale: 0.9,
    x: 500,
    y: 100,
    ease: 'power4.out',
  },
  {
    selector: '#screen-time',
    duration: 3,
    scale: 0.9,
    x: -500,
    y: -300,
    ease: 'power2.out',
  },
  {
    selector: '#weather',
    duration: 3,
    scale: 1.1,
    x: -400,
    y: 350,
    ease: 'power4.out',
  },
  {
    selector: '#stocks',
    duration: 3,
    scale: 0.9,
    x: 530,
    y: -170,
    ease: 'power4.out',
  },
  {
    selector: '#fitness',
    duration: 3,
    scale: 1.1,
    x: -350,
    y: -100,
    ease: 'power2.out',
  },
  {
    selector: '#find-my',
    duration: 3,
    scale: 1.1,
    x: 400,
    y: -360,
    ease: 'power4.out',
  },
  {
    selector: '#calendar',
    duration: 3,
    scale: 0.9,
    x: -630,
    y: 0,
    ease: 'power2.out',
  },
  {
    selector: '#wallet',
    duration: 3,
    scale: 1,
    x: -280,
    y: 100,
    ease: 'power4.out',
  },
  {
    selector: '#apple-tv',
    duration: 3,
    scale: 1,
    x: 500,
    y: 300,
    ease: 'power4.out',
  },
  {
    selector: '#sleep',
    duration: 3,
    scale: 0.9,
    x: 270,
    y: -50,
    ease: 'power2.out',
  },
  {
    selector: '#socials',
    duration: 3,
    scale: 1,
    x: 330,
    y: 120,
    ease: 'power2.out',
  },
];
