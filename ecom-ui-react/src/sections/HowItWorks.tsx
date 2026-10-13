type Tone = 'muted' | 'default' | 'highlight'

type Step = {
  number: string
  title: string
  description: string
  tone: Tone
}

const STEPS: Step[] = [
  {
    number: '01',
    title: 'Search',
    description: 'Find a route, rate or existing shipment without signing in.',
    tone: 'muted',
  },
  {
    number: '02',
    title: 'Compare',
    description: 'Review available sailings, transit time and operational deadlines.',
    tone: 'muted',
  },
  {
    number: '03',
    title: 'Choose',
    description: 'Select the sailing that best fits your cargo plan.',
    tone: 'default',
  },
  {
    number: '04',
    title: 'Book',
    description: 'Sign in or register only at the booking step.',
    tone: 'highlight',
  },
]

// Per-card colours as specified in the design.
const toneClasses: Record<Tone, { card: string; number: string; title: string; description: string }> = {
  muted: {
    card: 'bg-white border-black/10',
    number: 'text-ink-gray',
    title: 'text-black',
    description: 'text-ink-gray opacity-50',
  },
  default: {
    card: 'bg-white border-black/10',
    number: 'text-ink-muted',
    title: 'text-black',
    description: 'text-navy opacity-50',
  },
  highlight: {
    card: 'bg-brand/5 border-black/[0.06]',
    number: 'text-navy-deep',
    title: 'text-navy-deep',
    description: 'text-navy',
  },
}

export default function HowItWorks() {
  return (
    <section className="flex flex-col items-center gap-[20px] py-[80px]">
      <div className="shell-narrow flex flex-col gap-[6px] text-center">
        <p className="text-[18px] text-black opacity-60">How it Works</p>
        <h2 className="text-[24px] font-normal text-black md:text-[30px]">
          From search to shipment in a clear flow
        </h2>
      </div>

      <ol className="shell-narrow grid grid-cols-1 gap-[22px] sm:grid-cols-2 xl:grid-cols-4">
        {STEPS.map((step) => {
          const tone = toneClasses[step.tone]
          return (
            <li
              key={step.number}
              className={`flex items-start gap-[14px] rounded-[8px] border p-[14px] ${tone.card}`}
            >
              <span className={`text-[14px] ${tone.number}`}>{step.number}</span>
              <div className="flex min-w-0 flex-1 flex-col gap-[6px] text-[12px]">
                <h3 className={`font-semibold ${tone.title}`}>{step.title}</h3>
                <p className={tone.description}>{step.description}</p>
              </div>
            </li>
          )
        })}
      </ol>
    </section>
  )
}
